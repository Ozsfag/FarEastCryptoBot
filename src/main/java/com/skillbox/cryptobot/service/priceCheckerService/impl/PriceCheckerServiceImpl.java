package com.skillbox.cryptobot.service.priceCheckerService.impl;

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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Scope("prototype")
public class PriceCheckerServiceImpl implements PriceCheckerService {
    private final CheckingConfiguration checkingConfiguration;
    private final Timer timer;
    private final CrudService crudService;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final MessageTextConfiguration messageTextConfiguration;



    public PriceCheckerServiceImpl(CheckingConfiguration checkingConfiguration, CrudService crudService, CryptoCurrencyService cryptoCurrencyService, MessageTextConfiguration messageTextConfiguration) {
        this.checkingConfiguration = checkingConfiguration;
        this.timer = new Timer(true);
        this.crudService = crudService;
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.messageTextConfiguration = messageTextConfiguration;
        startPriceChecking();
    }

    private void startPriceChecking() {
        long delay = checkingConfiguration.getDelay() * 60 * 1000;
        long period = checkingConfiguration.getCheckingFrequency() * 60 * 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkPriceForAllSubscribers();
            }
        }, delay, period);
    }

    private void checkPriceForAllSubscribers() {
        // Assuming you have a method to get all subscribers
        Collection<Subscriber> subscribers = crudService.getAllSubscribers();
        subscribers.forEach(this::checkPrice);
    }

    private void checkPrice(Subscriber subscriber) {
        try {
            Double currentPrice = cryptoCurrencyService.getBitcoinPrice();
            Double subscriptionPrice = crudService.getPriceBySubscriber(subscriber);
            if (currentPrice >= subscriptionPrice) {
                String notification = String.format(messageTextConfiguration.getCheckNotification(), currentPrice);

                SendMessage message = SendMessageFactory.createSendMessage(subscriber.getTelegramId(), notification);


            }
        } catch (IOException e) {
            String notification = messageTextConfiguration.getGetPriceDisconnectMessage();
        }
    }
}
