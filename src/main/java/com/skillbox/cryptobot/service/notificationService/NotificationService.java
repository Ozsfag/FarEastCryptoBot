package com.skillbox.cryptobot.service.notificationService;

import com.skillbox.cryptobot.model.Subscriber;

public interface NotificationService {
    void scheduleNotification(Subscriber subscriber, Double currentPrice);
}
