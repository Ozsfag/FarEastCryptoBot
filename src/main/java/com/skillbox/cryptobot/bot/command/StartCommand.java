package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.utils.answerExecutorUtil.AnswerExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/** Обработка команды начала работы с ботом */
@Service
@Lazy
@Slf4j
public class StartCommand implements IBotCommand {
  private final CrudService crudService;
  private final MessageTextConfiguration messageTextConfiguration;
  private final SendMessageFactory sendMessageFactory;

  public StartCommand(
      CrudService crudService,
      MessageTextConfiguration messageTextConfiguration,
      SendMessageFactory sendMessageFactory) {
    this.crudService = crudService;
    this.messageTextConfiguration = messageTextConfiguration.clone();
    this.sendMessageFactory = sendMessageFactory;
  }

  @Override
  public String getCommandIdentifier() {
    return messageTextConfiguration.getStartCommandIdentifier();
  }

  @Override
  public String getDescription() {
    return messageTextConfiguration.getStartCommandDescription();
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    crudService.createUser(message);

    SendMessage answer =
        sendMessageFactory.createSendMessage(
            message.getChatId(), messageTextConfiguration.getStartGreetingsMessage());
    AnswerExecutorUtil.executeAnswer(
        absSender, answer, messageTextConfiguration.getStartErrorMessage());
  }
}
