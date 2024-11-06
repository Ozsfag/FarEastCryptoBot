package com.skillbox.cryptobot.service.entityFactoryService;

import com.skillbox.cryptobot.model.Subscriber;

public interface EntityFactoryService {
    Subscriber createSubscriber(Long tId, Double price);
}
