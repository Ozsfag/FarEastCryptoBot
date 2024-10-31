package com.skillbox.cryptobot.service.crudService.impl;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.service.entityFactoryService.EntityFactoryService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

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
  public void createUser(Message message, Double price) {
    Long telegramId = getUserTelegramId(message);
    if (!subscriberRepository.existsByTelegramId(telegramId)) {
      Subscriber subscriber = entityFactoryService.createSubscriber(telegramId, price);
      subscriberRepository.saveAndFlush(subscriber);
    }
  }

  @Override
  public void updateUser(Message message, Double price) {
    Long telegramId = getUserTelegramId(message);
    subscriberRepository.updatePriceByTelegramId(price, telegramId);
  }

  private Long getUserTelegramId(Message message) {
    return message.getFrom().getId();
  }
}
