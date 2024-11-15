package com.skillbox.cryptobot.factory;

import com.skillbox.cryptobot.model.Subscriber;
import org.springframework.stereotype.Component;

@Component
public class EntityFactory {
  public Subscriber createSubscriber(Long tId, Double price) {
    return Subscriber.builder().telegramId(tId).price(price).build();
  }
}
