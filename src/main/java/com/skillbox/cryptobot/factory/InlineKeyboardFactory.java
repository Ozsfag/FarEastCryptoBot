package com.skillbox.cryptobot.factory;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class InlineKeyboardFactory {
  private final MessageTextConfiguration messageTextConfiguration;

  public InlineKeyboardFactory(MessageTextConfiguration messageTextConfiguration) {
    this.messageTextConfiguration = messageTextConfiguration.clone();
  }

  public InlineKeyboardMarkup createInlineKB() {

    List<List<InlineKeyboardButton>> rowsInline =
        Stream.of(
                createButtonRow(messageTextConfiguration.getStartCommandIdentifier(), "Start"),
                createButtonRow(
                    messageTextConfiguration.getGetPriceCommandIdentifier(), "Get Price"),
                createButtonRow(
                    messageTextConfiguration.getSubscribeCommandIdentifier(), "Subscribe"),
                createButtonRow(
                    messageTextConfiguration.getGetSubscriptionCommandIdentifier(),
                    "Get Subscription"),
                createButtonRow(
                    messageTextConfiguration.getUnsubscribeCommandIdentifier(), "Unsubscribe"))
            .toList();

    return InlineKeyboardMarkup.builder().keyboard(rowsInline).build();
  }

  private List<InlineKeyboardButton> createButtonRow(String commandIdentifier, String buttonText) {
    if (commandIdentifier == null || commandIdentifier.isEmpty()) {
      throw new IllegalArgumentException("Command identifier cannot be null or empty");
    }

    InlineKeyboardButton button =
        InlineKeyboardButton.builder().text(buttonText).callbackData(commandIdentifier).build();

    return List.of(button);
  }
}
