package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.crudService.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/** Обработка команды подписки на курс валюты */
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {
  private final CrudService crudService;

  public SubscribeCommand(CrudService crudService) {
    this.crudService = crudService;
  }

  @Override
  public String getCommandIdentifier() {
    return "subscribe";
  }

  @Override
  public String getDescription() {
    return "Подписывает пользователя на стоимость биткоина";
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    crudService.updateUser(message, Double.valueOf(message.toString()));

    SendMessage answer = new SendMessage();
    answer.setChatId(message.getChatId());

    answer.setText(
        """
                Привет! Данный бот помогает отслеживать стоимость биткоина.
                Поддерживаемые команды:
                 /get_price - получить стоимость биткоина
                """);
    try {
      absSender.execute(answer);
    } catch (TelegramApiException e) {
      log.error("Error occurred in /start command", e);
    }
  }
}
