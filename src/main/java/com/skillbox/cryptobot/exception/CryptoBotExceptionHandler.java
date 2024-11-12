package com.skillbox.cryptobot.exception;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class CryptoBotExceptionHandler {
    public static void handleTelegramApiException(String errorMessage, TelegramApiException e) {
        log.error(errorMessage, e);
    }
}
