package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.utils.MapperUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

/** Обработка команды отмены подписки на курс валюты */
@Service
@Slf4j
public class UnsubscribeCommand implements IBotCommand {
  private final SubscriberRepository subscriberRepository;
  private final CrudService crudService;

    public UnsubscribeCommand(SubscriberRepository subscriberRepository, CrudService crudService) {
        this.subscriberRepository = subscriberRepository;
        this.crudService = crudService;
    }

    @Override
  public String getCommandIdentifier() {
    return "unsubscribe";
  }

  @Override
  public String getDescription() {
    return "Отменяет подписку пользователя";
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    SendMessage answer = new SendMessage();
    answer.setChatId(message.getChatId());

    Long telegramId = message.getFrom().getId();
    Double price = subscriberRepository.findByTelegramId(telegramId);

    String responseText = Objects.isNull(price) ?
            "Активная подписка отсутствует" :
            "Подписка отменена";

    if (!Objects.isNull(price)) {
      crudService.updateUser(message, Double.NaN);
    }

    answer.setText(responseText);

    try {
      absSender.execute(answer);
    } catch (TelegramApiException e) {
      log.error("Error occurred in /unsubscribe command", e);
    }
  }
}
