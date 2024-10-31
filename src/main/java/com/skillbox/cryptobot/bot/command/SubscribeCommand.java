package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.utils.MapperUtil;
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
  private final GetPriceCommand getPriceCommand;
  private final CrudService crudService;

  public SubscribeCommand(GetPriceCommand getPriceCommand, CrudService crudService) {
    this.getPriceCommand = getPriceCommand;
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

    getPriceCommand.processMessage(absSender, message, arguments);
    crudService.updateUser(message, MapperUtil.getConvertedPrice(message));

    SendMessage answer = new SendMessage();
    answer.setChatId(message.getChatId());

    answer.setText(
        "Новая подписка создана на стоимость " + MapperUtil.getConvertedPrice(message) + " USD");
    try {
      absSender.execute(answer);
    } catch (TelegramApiException e) {
      log.error("Error occurred in /subscribe command", e);
    }
  }
}
