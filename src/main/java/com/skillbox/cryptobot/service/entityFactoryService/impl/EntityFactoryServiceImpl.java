package com.skillbox.cryptobot.service.entityFactoryService.impl;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.entityFactoryService.EntityFactoryService;
import org.springframework.stereotype.Service;

@Service
public class EntityFactoryServiceImpl implements EntityFactoryService {

    @Override
    public Subscriber createSubscriber(Integer uuid, Integer tId, String price) {
        return Subscriber.builder()
                .uuid(uuid)
                .tId(tId)
                .price(price)
                .build();
    }
}
