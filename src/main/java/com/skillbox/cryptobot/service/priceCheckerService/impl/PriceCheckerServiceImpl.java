package com.skillbox.cryptobot.service.priceCheckerService.impl;

import com.skillbox.cryptobot.configuration.CheckingConfiguration;
import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.service.cryptoCurrencyService.CryptoCurrencyService;
import com.skillbox.cryptobot.service.priceCheckerService.PriceCheckerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Scope("prototype")
public class PriceCheckerServiceImpl implements PriceCheckerService {
    private final CryptoCurrencyService cryptoCurrencyService;
    private final CrudService crudService;
    private final CheckingConfiguration checkingConfiguration;
    private final Timer timer;

    public PriceCheckerServiceImpl(CryptoCurrencyService cryptoCurrencyService, CrudService crudService, CheckingConfiguration checkingConfiguration) {
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.crudService = crudService;
        this.checkingConfiguration = checkingConfiguration;
        this.timer = new Timer(true);
//        startPriceChecking();
    }

    private void startPriceChecking() {
        long delay = 0; // Start immediately
        long period = checkingConfiguration.getCheckingFrequency() * 60 * 1000; // Convert minutes to milliseconds
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
        for (Subscriber subscriber : subscribers) {
            checkPrice(subscriber);
        }
    }

    private void checkPrice(Message message) {
        try {
            double currentPrice = cryptoCurrencyService.getBitcoinPrice();
            double subscriptionPrice = crudService.getPrice(message);
            if (currentPrice >= subscriptionPrice) {
                // Send notification
                String notification = String.format(checkingConfiguration.getCheckNotification(), currentPrice);
                // Code to send the notification to the user
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
