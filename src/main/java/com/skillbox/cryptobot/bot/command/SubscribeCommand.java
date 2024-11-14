package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.utils.answerExecutorUtil.AnswerExecutorUtil;
import com.skillbox.cryptobot.utils.mapperUtil.impl.MapperUtilImpl;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/** Обработка команды подписки на курс валюты */
@Service
@Lazy
@Slf4j
public class SubscribeCommand implements IBotCommand {
  private final GetPriceCommand getPriceCommand;
  private final CrudService crudService;
  private final MapperUtilImpl mapperUtil;
  private final SendMessageFactory sendMessageFactory;
  private final MessageTextConfiguration messageTextConfiguration;

  public SubscribeCommand(
      GetPriceCommand getPriceCommand,
      CrudService crudService,
      MapperUtilImpl mapperUtil,
      SendMessageFactory sendMessageFactory,
      MessageTextConfiguration messageTextConfiguration) {
    this.getPriceCommand = getPriceCommand;
    this.crudService = crudService;
    this.mapperUtil = mapperUtil;
    this.sendMessageFactory = sendMessageFactory;
    this.messageTextConfiguration = messageTextConfiguration.clone();
  }

  @Override
  public String getCommandIdentifier() {
    return messageTextConfiguration.getSubscribeCommandIdentifier();
  }

  @Override
  public String getDescription() {
    return messageTextConfiguration.getSubscribeCommandDescription();
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    getPriceCommand.processMessage(absSender, message, arguments);
    Double price = mapperUtil.getConvertedFromMessageBitcoinPrice(message);
    crudService.updateUser(message, price);
    String text = getText(price);

    SendMessage answer = sendMessageFactory.createSendMessage(message.getChatId(), text);
    AnswerExecutorUtil.executeAnswer(
        absSender, answer, messageTextConfiguration.getSubscribeMessage());
  }

  private String getText(Double price) {
    return Objects.isNull(price)
        ? messageTextConfiguration.getSubscribeWrongInputMessage()
        : String.format(messageTextConfiguration.getSubscribeMessage(), price);
  }
}
