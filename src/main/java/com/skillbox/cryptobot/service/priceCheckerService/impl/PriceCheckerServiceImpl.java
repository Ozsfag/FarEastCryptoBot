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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PriceCheckerServiceImpl implements PriceCheckerService {
    private final CheckingConfiguration checkingConfiguration;
    private final Timer timer;
    private final CrudService crudService;
    private final MapperUtil mapperUtil;
    private final MessageTextConfiguration messageTextConfiguration;
    private final CryptoBot cryptoBot;
    private final SendMessageFactory sendMessageFactory;


    public PriceCheckerServiceImpl(CheckingConfiguration checkingConfiguration, CrudService crudService, MapperUtil mapperUtil, MessageTextConfiguration messageTextConfiguration, CryptoBot cryptoBot, SendMessageFactory sendMessageFactory) {
        this.checkingConfiguration = checkingConfiguration;
        this.mapperUtil = mapperUtil;
        this.cryptoBot = cryptoBot;
        this.sendMessageFactory = sendMessageFactory;
        this.timer = new Timer(true);
        this.crudService = crudService;
        this.messageTextConfiguration = messageTextConfiguration;
        startPriceChecking();
    }

    @PostConstruct
    @Override
    public void startPriceChecking() {
        long delay = TimeUnit.MINUTES.toMillis(checkingConfiguration.getDelay());
        long period = TimeUnit.MINUTES.toMillis(checkingConfiguration.getCheckingFrequency());
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkPriceForAllSubscribers();
            }
        }, delay, period);
    }

    private void checkPriceForAllSubscribers() {
        Collection<Subscriber> subscribers = crudService.getAllSubscribers();
        subscribers.forEach(this::checkPrice);
    }

    private void checkPrice(Subscriber subscriber) {
        Optional<Double> currentPriceOptional = mapperUtil.getPrice();

        Double currentPrice = currentPriceOptional.get();

        Double subscriptionPrice = crudService.getPriceBySubscriber(subscriber);
        if (currentPrice <= subscriptionPrice) {
            sendNotification(subscriber, currentPrice);
        }
    }


    private void sendNotification(Subscriber subscriber, Double currentPrice) {
        String notification = String.format(messageTextConfiguration.getCheckNotification(), currentPrice);
        SendMessage message = sendMessageFactory.createSendMessage(subscriber.getTelegramId(), notification);
        try {
            cryptoBot.executeAsync(message);
        } catch (TelegramApiException e) {
            String errorMessage = String.format(messageTextConfiguration.getCheckNotificationErrorMessage(), subscriber.getTelegramId());
            CryptoBotExceptionHandler.handleTelegramApiException(errorMessage, e);
        }
    }
}
