package com.skillbox.cryptobot.service.crudService.impl;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.service.entityFactoryService.EntityFactoryService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Collection;

@Service
public class CrudServiceImpl implements CrudService {
  private final EntityFactoryService entityFactoryService;
  private final SubscriberRepository subscriberRepository;

  public CrudServiceImpl(
      EntityFactoryService entityFactoryService, SubscriberRepository subscriberRepository) {
    this.entityFactoryService = entityFactoryService;
    this.subscriberRepository = subscriberRepository;
  }

  @Override
  public void createUser(Message message) {
    Long telegramId = getUserTelegramId(message);
    if (!subscriberRepository.existsByTelegramId(telegramId)) {
      Subscriber subscriber = entityFactoryService.createSubscriber(telegramId, null);
      subscriberRepository.saveAndFlush(subscriber);
    }
  }

  @Override
  public void updateUser(Message message, Double price) {
    Long telegramId = getUserTelegramId(message);
    subscriberRepository.updatePriceByTelegramId(price, telegramId);
  }

  @Override
  public Double getPriceByMessage(Message message) {
    Long telegramId = getUserTelegramId(message);
    return subscriberRepository.findByTelegramId(telegramId);
  }

  @Override
  public Collection<Subscriber> getAllSubscribers(){
    return subscriberRepository.findByPriceNotNull();
  }

  @Override
  public Double getPriceBySubscriber(Subscriber subscriber) {
    return subscriber.getPrice();
  }
  private Long getUserTelegramId(Message message) {
    return message.getFrom().getId();
  }
}
