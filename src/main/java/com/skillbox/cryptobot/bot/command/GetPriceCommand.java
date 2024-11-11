package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.service.cryptoCurrencyService.CryptoCurrencyService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/** Обработка команды получения текущей стоимости валюты */
@Service
@Lazy
@Slf4j
public class GetPriceCommand implements IBotCommand {

  private final CryptoCurrencyService service;
  private final MessageTextConfiguration messageTextConfiguration;

  public GetPriceCommand(
      CryptoCurrencyService service, MessageTextConfiguration messageTextConfiguration) {
    this.service = service;
    this.messageTextConfiguration = messageTextConfiguration.clone();
  }

  @Override
  public String getCommandIdentifier() {
    return messageTextConfiguration.getGetPriceCommandIdentifier();
  }

  @Override
  public String getDescription() {
    return messageTextConfiguration.getGetPriceCommandDescription();
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    try {
      Double price = service.getBitcoinPrice();
      SendMessage answer =
          SendMessageFactory.createSendMessage(
              message.getChatId(),
              String.format(messageTextConfiguration.getGetPriceMessage(), price));
      executeAnswer(absSender, answer);
    } catch (IOException e) {
      log.error("Failed to retrieve Bitcoin price due to network issues", e);
      SendMessage errorMessage =
          SendMessageFactory.createSendMessage(
              message.getChatId(), messageTextConfiguration.getGetPriceDisconnectMessage());
      executeAnswer(absSender, errorMessage);
    }
  }

  private void executeAnswer(AbsSender absSender, SendMessage answer) {
    try {
      absSender.execute(answer);
    } catch (Exception e) {
      log.error(messageTextConfiguration.getGetPriceErrorMessage(), e);
    }
  }
}
