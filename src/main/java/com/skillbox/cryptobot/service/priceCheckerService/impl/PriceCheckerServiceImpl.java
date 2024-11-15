package com.skillbox.cryptobot.service.priceCheckerService.impl;

import com.skillbox.cryptobot.configuration.CheckingConfiguration;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.service.notificationService.NotificationService;
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

@Service
@Slf4j
public class PriceCheckerServiceImpl implements PriceCheckerService {
  private final CheckingConfiguration checkingConfiguration;
  private final ScheduledExecutorService scheduler;
  private final CrudService crudService;
  private final MapperUtil mapperUtil;
  private final Set<Long> notifiedSubscribers;
  private final NotificationService notificationService;

  public PriceCheckerServiceImpl(
          CheckingConfiguration checkingConfiguration,
          ScheduledExecutorService scheduler,
          CrudService crudService,
          MapperUtil mapperUtil, NotificationService notificationService) {
    this.checkingConfiguration = checkingConfiguration.clone();
    this.scheduler = scheduler;
    this.crudService = crudService;
    this.mapperUtil = mapperUtil;
    this.notificationService = notificationService;
    this.notifiedSubscribers = ConcurrentHashMap.newKeySet();
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
                            notificationService.scheduleNotification(subscriber, currentPrice);
                          }
                        }));
  }

  private boolean isPriceBelowThreshold(Subscriber subscriber, Double currentPrice) {
    Double subscriptionPrice = crudService.getPriceBySubscriber(subscriber);
    return currentPrice <= subscriptionPrice;
  }

}
