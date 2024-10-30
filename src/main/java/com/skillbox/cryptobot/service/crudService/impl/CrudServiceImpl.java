package com.skillbox.cryptobot.service.crudService.impl;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.entityFactoryService.EntityFactoryService;
import com.skillbox.cryptobot.service.crudService.CrudService;
import org.springframework.stereotype.Service;

@Service
public class CrudServiceImpl implements CrudService {
    private final EntityFactoryService entityFactoryService;
    private final SubscriberRepository subscriberRepository;

    public CrudServiceImpl(EntityFactoryService entityFactoryService, SubscriberRepository subscriberRepository) {
        this.entityFactoryService = entityFactoryService;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public void createUser(Long tId, Double price) {
        Subscriber subscriber = entityFactoryService.createSubscriber(tId, price);
        subscriberRepository.saveAndFlush(subscriber);
    }

    @Override
    public void updateUser(Long tId, Double price) {
        subscriberRepository.updatePriceBy(price);
    }
}
