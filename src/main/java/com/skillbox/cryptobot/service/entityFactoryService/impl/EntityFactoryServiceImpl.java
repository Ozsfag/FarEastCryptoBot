package com.skillbox.cryptobot.service.entityFactoryService.impl;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.entityFactoryService.EntityFactoryService;
import org.springframework.stereotype.Service;

@Service
public class EntityFactoryServiceImpl implements EntityFactoryService {

    @Override
    public Subscriber createSubscriber(Long tId, Double price) {
        return Subscriber.builder()
                .tId(tId)
                .price(price)
                .build();
    }
}
