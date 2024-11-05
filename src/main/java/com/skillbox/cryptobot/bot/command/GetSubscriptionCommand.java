package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.repository.SubscriberRepository;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j

public class GetSubscriptionCommand implements IBotCommand {
  private final SubscriberRepository subscriberRepository;

    public GetSubscriptionCommand(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
  public String getCommandIdentifier() {
    return "get_subscription";
  }

  @Override
  public String getDescription() {
    return "Возвращает текущую подписку";
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    Long telegramId = message.getFrom().getId();
    Double price = subscriberRepository.findByTelegramId(telegramId);

    SendMessage answer = new SendMessage();
    answer.setChatId(message.getChatId());

    String responseText = Objects.isNull(price) ?
            "Активные подписки отсутствуют" :
            "Вы подписаны на стоимость биткоина " + price + " USD";
    answer.setText(responseText);
    try {
      absSender.execute(answer);
    } catch (TelegramApiException e) {
      log.error("Error occurred in /get_subscription command", e);
    }
  }
}
