package com.skillbox.cryptobot.service.priceCheckerService.impl;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.configuration.CheckingConfiguration;
import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.service.cryptoCurrencyService.CryptoCurrencyService;
import com.skillbox.cryptobot.service.priceCheckerService.PriceCheckerService;
import java.io.IOException;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class PriceCheckerServiceImpl implements PriceCheckerService {
    private final CheckingConfiguration checkingConfiguration;
    private final Timer timer;
    private final CrudService crudService;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final MessageTextConfiguration messageTextConfiguration;
    private final CryptoBot cryptoBot;



    public PriceCheckerServiceImpl(CheckingConfiguration checkingConfiguration, CrudService crudService, CryptoCurrencyService cryptoCurrencyService, MessageTextConfiguration messageTextConfiguration, CryptoBot cryptoBot) {
        this.checkingConfiguration = checkingConfiguration;
        this.cryptoBot = cryptoBot;
        this.timer = new Timer(true);
        this.crudService = crudService;
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.messageTextConfiguration = messageTextConfiguration;
        startPriceChecking();
    }
    @PostConstruct
    @Override
    public  void startPriceChecking() {
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
        try {
            Double currentPrice = getCurrentPrice();
            Double subscriptionPrice = crudService.getPriceBySubscriber(subscriber);
            if (currentPrice >= subscriptionPrice) {
                sendNotification(subscriber, currentPrice);
            }
        } catch (TelegramApiException e) {
            log.error(messageTextConfiguration.getCheckNotificationErrorMessage(), e);
        }
    }

    private Double getCurrentPrice() throws TelegramApiException {
        try {
            return cryptoCurrencyService.getBitcoinPrice();
        } catch (IOException e) {
            log.error("Failed to retrieve Bitcoin price", e);
            throw new TelegramApiException("Failed to retrieve Bitcoin price", e);
        }
    }

    private void sendNotification(Subscriber subscriber, Double currentPrice) {
        String notification = String.format(messageTextConfiguration.getCheckNotification(), currentPrice);
        SendMessage message = SendMessageFactory.createSendMessage(subscriber.getTelegramId(), notification);
        try {
            cryptoBot.executeAsync(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send notification to subscriber: {}", subscriber.getTelegramId(), e);
        }
    }
}
