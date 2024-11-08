package com.skillbox.cryptobot.factory;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMessageFactory {

  public static SendMessage createSendMessage(Long chatId, String text) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText(text);
    return sendMessage;
  }
}
