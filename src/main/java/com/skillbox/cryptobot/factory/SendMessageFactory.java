package com.skillbox.cryptobot.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class SendMessageFactory {
    private final InlineKeyboardFactory inlineKeyboardFactory;

    public SendMessageFactory(InlineKeyboardFactory inlineKeyboardFactory) {
        this.inlineKeyboardFactory = inlineKeyboardFactory;
    }

    public SendMessage createSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardFactory.createInlineKB())
                .parseMode("HTML")
                .build();
    }
}
