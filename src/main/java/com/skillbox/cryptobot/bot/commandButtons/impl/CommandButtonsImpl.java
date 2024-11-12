package com.skillbox.cryptobot.bot.commandButtons.impl;

import com.skillbox.cryptobot.bot.commandButtons.CommandButtons;
import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CommandButtonsImpl implements CommandButtons {
    private final MessageTextConfiguration messageTextConfiguration;

    public CommandButtonsImpl(MessageTextConfiguration messageTextConfiguration) {
        this.messageTextConfiguration = messageTextConfiguration;
    }

    @Override
    public InlineKeyboardMarkup createCommandButtons() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = Stream.of(
                        createButtonRow(messageTextConfiguration.getStartCommandIdentifier(), "Start"),
                        createButtonRow(messageTextConfiguration.getGetPriceCommandIdentifier(), "Get Price"),
                        createButtonRow(messageTextConfiguration.getSubscribeCommandIdentifier(), "Subscribe"),
                        createButtonRow(messageTextConfiguration.getGetSubscriptionCommandIdentifier(), "Get Subscription"),
                        createButtonRow(messageTextConfiguration.getUnsubscribeCommandIdentifier(), "Unsubscribe"))
                .toList();

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private List<InlineKeyboardButton> createButtonRow(String commandIdentifier, String buttonText) {
        if (commandIdentifier == null || commandIdentifier.isEmpty()) {
            throw new IllegalArgumentException("Command identifier cannot be null or empty");
        }

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setCallbackData(commandIdentifier);

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(button);
        return rowInline;
    }
}
