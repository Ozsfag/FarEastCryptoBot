package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.service.crudService.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

@Service
@Lazy
@Slf4j

public class GetSubscriptionCommand implements IBotCommand {
    private final CrudService crudService;
    private final MessageTextConfiguration messageTextConfiguration;

    public GetSubscriptionCommand(CrudService crudService, MessageTextConfiguration messageTextConfiguration) {
        this.crudService = crudService;
        this.messageTextConfiguration = messageTextConfiguration.clone();
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
        Double price = crudService.getPrice(message);
        String text = getText(price);

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText(text);

        executeAnswer(absSender, answer);
    }
    private String getText(Double price){
        return Objects.isNull(price) ?
                messageTextConfiguration.getGetNonActiveSubscriptionMessage() :
                String.format(messageTextConfiguration.getGetSubscriptionMessage(), price);
    }
    private void executeAnswer(AbsSender absSender, SendMessage answer){
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error(messageTextConfiguration.getGetSubscriptionErrorMessage(), e);
        }
    }
}
