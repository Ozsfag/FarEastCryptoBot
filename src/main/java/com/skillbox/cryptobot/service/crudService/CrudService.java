package com.skillbox.cryptobot.service.crudService;

import com.skillbox.cryptobot.model.Subscriber;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Collection;

public interface CrudService {
  void createUser(Message message);

  void updateUser(Message message, Double price);

  Double getPriceByMessage(Message message);

  Collection<Subscriber> getAllSubscribers();

  Double getPriceBySubscriber(Subscriber subscriber);
}
