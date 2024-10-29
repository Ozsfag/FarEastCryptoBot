package com.skillbox.cryptobot.service.userAddingService.impl;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.entityFactoryService.EntityFactoryService;
import com.skillbox.cryptobot.service.userAddingService.UserAddingService;
import org.springframework.stereotype.Service;

@Service
public class UserAddingServiceImpl implements UserAddingService {
    private final EntityFactoryService entityFactoryService;
    private final SubscriberRepository subscriberRepository;

    public UserAddingServiceImpl(EntityFactoryService entityFactoryService, SubscriberRepository subscriberRepository) {
        this.entityFactoryService = entityFactoryService;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public void addUser(Integer uuid, Integer tId, String price) {
        Subscriber subscriber = entityFactoryService.createSubscriber(uuid, tId, price);
        subscriberRepository.saveAndFlush(subscriber);
    }
}
