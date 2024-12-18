package com.skillbox.cryptobot.service.notificationService.impl;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.configuration.CheckingConfiguration;
import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.exception.CryptoBotExceptionHandler;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.notificationService.NotificationService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final Set<Long> notifiedSubscribers;
    private final CheckingConfiguration checkingConfiguration;
    private final ScheduledExecutorService scheduler;
    private final MessageTextConfiguration messageTextConfiguration;
    private final SendMessageFactory sendMessageFactory;
    private final CryptoBot cryptoBot;


    public NotificationServiceImpl(ScheduledExecutorService scheduler, CheckingConfiguration checkingConfiguration, CryptoBot cryptoBot,
                                   SendMessageFactory sendMessageFactory, MessageTextConfiguration messageTextConfiguration) {
        this.notifiedSubscribers = ConcurrentHashMap.newKeySet();
        this.checkingConfiguration = checkingConfiguration.clone();
        this.scheduler = scheduler;
        this.messageTextConfiguration = messageTextConfiguration.clone();
        this.sendMessageFactory = sendMessageFactory;
        this.cryptoBot = cryptoBot;
    }

    @Override
    public void scheduleNotification(Subscriber subscriber, Double currentPrice) {
        if (notifiedSubscribers.add(subscriber.getTelegramId())) {
            Integer notificationDelay = checkingConfiguration.getNotificationDelay();
            Integer notificationPeriod = checkingConfiguration.getNotificationFrequency();
            scheduler.scheduleAtFixedRate(
                    () -> sendNotification(subscriber, currentPrice),
                    notificationDelay,
                    notificationPeriod,
                    TimeUnit.MINUTES);
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
        } finally {
            notifiedSubscribers.remove(subscriber.getTelegramId());
        }
    }
}
