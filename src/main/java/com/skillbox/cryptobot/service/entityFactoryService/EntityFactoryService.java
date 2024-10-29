package com.skillbox.cryptobot.service.entityFactoryService;

import com.skillbox.cryptobot.model.Subscriber;

public interface EntityFactoryService {
    Subscriber createSubscriber(Integer uuid, Integer tId, String price);
}
