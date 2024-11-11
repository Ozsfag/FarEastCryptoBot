package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.service.crudService.CrudService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Lazy
@Slf4j
public class UnsubscribeCommand implements IBotCommand {
    private final CrudService crudService;
    private final MessageTextConfiguration messageTextConfiguration;

    public UnsubscribeCommand(CrudService crudService, MessageTextConfiguration messageTextConfiguration) {
        this.crudService = crudService;
        this.messageTextConfiguration = messageTextConfiguration.clone();
    }

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        Double price = crudService.getPriceByMessage(message);
        String text = getText(price);

        updateToDefaultValueIfExist(price, message);

        SendMessage answer = SendMessageFactory.createSendMessage(message.getChatId(), text);
        executeAnswer(absSender, answer);
    }
    private String getText(Double price){
        return Objects.isNull(price) ?
                messageTextConfiguration.getGetNonActiveSubscriptionMessage() :
                messageTextConfiguration.getUnsubscribeMessage();
    }
    private void updateToDefaultValueIfExist(Double price, Message message){
        if (Objects.nonNull(price)) {
            crudService.updateUser(message, Double.NaN);
        }
    }
    private void executeAnswer(AbsSender absSender, SendMessage answer){
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error(messageTextConfiguration.getUnsubscribeErrorMessage(), e);
        }
    }
}
