package com.skillbox.cryptobot.service.crudService;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CrudService {
  void createUser(Message message, Double price);

  void updateUser(Message message, Double price);
}
