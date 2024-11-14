package com.skillbox.cryptobot.service.priceCheckerService.impl;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.configuration.CheckingConfiguration;
import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.exception.CryptoBotExceptionHandler;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.service.priceCheckerService.PriceCheckerService;
import com.skillbox.cryptobot.utils.mapperUtil.MapperUtil;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class PriceCheckerServiceImpl implements PriceCheckerService {
  private final CheckingConfiguration checkingConfiguration;
  private final ScheduledExecutorService scheduler;
  private final CrudService crudService;
  private final MapperUtil mapperUtil;
  private final MessageTextConfiguration messageTextConfiguration;
  private final CryptoBot cryptoBot;
  private final SendMessageFactory sendMessageFactory;
  private final Set<Long> notifiedSubscribers = ConcurrentHashMap.newKeySet();

  public PriceCheckerServiceImpl(
      CheckingConfiguration checkingConfiguration,
      ScheduledExecutorService scheduler,
      CrudService crudService,
      MapperUtil mapperUtil,
      MessageTextConfiguration messageTextConfiguration,
      CryptoBot cryptoBot,
      SendMessageFactory sendMessageFactory) {
    this.checkingConfiguration = checkingConfiguration.clone();
    this.scheduler = scheduler;
    this.mapperUtil = mapperUtil;
    this.cryptoBot = cryptoBot;
    this.sendMessageFactory = sendMessageFactory;
    this.crudService = crudService;
    this.messageTextConfiguration = messageTextConfiguration.clone();
    startPriceChecking();
  }

  @PostConstruct
  @Override
  public void startPriceChecking() {
    Integer checkingDelay = checkingConfiguration.getCheckingDelay();
    Integer checkingPeriod = checkingConfiguration.getCheckingFrequency();
    scheduler.scheduleAtFixedRate(
        this::checkPriceForAllSubscribers, checkingDelay, checkingPeriod, TimeUnit.MINUTES);
  }

  private void checkPriceForAllSubscribers() {
    Collection<Subscriber> subscribers = crudService.getAllSubscribers();
    mapperUtil
        .getCurrentBitcoinPrice()
        .ifPresent(
            currentPrice ->
                subscribers.stream()
                    .filter(subscriber -> isPriceBelowThreshold(subscriber, currentPrice))
                    .forEach(
                        subscriber -> {
                          if (notifiedSubscribers.add(subscriber.getTelegramId())) {
                            scheduleNotification(subscriber, currentPrice);
                          }
                        }));
  }

  private boolean isPriceBelowThreshold(Subscriber subscriber, Double currentPrice) {
    Double subscriptionPrice = crudService.getPriceBySubscriber(subscriber);
    return currentPrice <= subscriptionPrice;
  }

  private void scheduleNotification(Subscriber subscriber, Double currentPrice) {
    Integer notificationDelay = checkingConfiguration.getNotificationDelay();
    Integer notificationPeriod = checkingConfiguration.getNotificationFrequency();
    scheduler.scheduleAtFixedRate(
        () -> sendNotification(subscriber, currentPrice),
        notificationDelay,
        notificationPeriod,
        TimeUnit.MINUTES);
  }

  private void sendNotification(Subscriber subscriber, Double currentPrice) {
    String notification =
        String.format(messageTextConfiguration.getCheckNotification(), currentPrice);
    SendMessage message =
        sendMessageFactory.createSendMessage(subscriber.getTelegramId(), notification);
    try {
      cryptoBot.executeAsync(message);
    } catch (TelegramApiException e) {
      String errorMessage =
          String.format(
              messageTextConfiguration.getCheckNotificationErrorMessage(),
              subscriber.getTelegramId());
      CryptoBotExceptionHandler.handleTelegramApiException(errorMessage, e);
    } finally {
      notifiedSubscribers.remove(subscriber.getTelegramId());
    }
  }
}
