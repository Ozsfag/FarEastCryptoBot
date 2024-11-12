package com.skillbox.cryptobot.factory;

import com.skillbox.cryptobot.bot.commandButtons.CommandButtons;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class SendMessageFactory {
    private final CommandButtons commandButtons;

    public SendMessageFactory(CommandButtons commandButtons) {
        this.commandButtons = commandButtons;
    }

    public SendMessage createSendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(commandButtons.createCommandButtons());
        return sendMessage;
    }
}
