package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.utils.answerExecutorUtil.AnswerExecutorUtil;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Lazy
@Slf4j
public class GetSubscriptionCommand implements IBotCommand {
  private final CrudService crudService;
  private final MessageTextConfiguration messageTextConfiguration;
  private final SendMessageFactory sendMessageFactory;

  public GetSubscriptionCommand(
      CrudService crudService,
      MessageTextConfiguration messageTextConfiguration,
      SendMessageFactory sendMessageFactory) {
    this.crudService = crudService;
    this.messageTextConfiguration = messageTextConfiguration.clone();
    this.sendMessageFactory = sendMessageFactory;
  }

  @Override
  public String getCommandIdentifier() {
    return messageTextConfiguration.getGetSubscriptionCommandIdentifier();
  }

  @Override
  public String getDescription() {
    return messageTextConfiguration.getGetSubscriptionCommandDescription();
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    Double price = crudService.getPriceByMessage(message);
    String text = getText(price);

    SendMessage answer = sendMessageFactory.createSendMessage(message.getChatId(), text);
    AnswerExecutorUtil.executeAnswer(
        absSender, answer, messageTextConfiguration.getGetSubscriptionErrorMessage());
  }

  private String getText(Double price) {
    return Objects.isNull(price)
        ? messageTextConfiguration.getGetNonActiveSubscriptionMessage()
        : String.format(messageTextConfiguration.getGetSubscriptionMessage(), price);
  }
}
