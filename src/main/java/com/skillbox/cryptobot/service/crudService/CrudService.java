package com.skillbox.cryptobot.service.crudService;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CrudService {
  void createUser(Message message);

  void updateUser(Message message, Double price);

  Double getPrice(Message message);
}
