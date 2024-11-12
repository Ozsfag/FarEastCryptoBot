package com.skillbox.cryptobot.utils.answerExecutor;

import com.skillbox.cryptobot.exception.CryptoBotExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class AnswerExecutor {
    public static void executeAnswer(AbsSender absSender, SendMessage answer, String message) {
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            CryptoBotExceptionHandler.handleTelegramApiException(message, e);
        }
    }
}
