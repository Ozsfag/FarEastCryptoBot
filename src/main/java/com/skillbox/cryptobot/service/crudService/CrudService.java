package com.skillbox.cryptobot.service.crudService;

import com.skillbox.cryptobot.model.Subscriber;
import java.util.Collection;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CrudService {
  void createUser(Message message);

  void updateUser(Message message, Double price);

  Double getPriceByMessage(Message message);

  Collection<Subscriber> getAllSubscribers();

  Double getPriceBySubscriber(Subscriber subscriber);
}
