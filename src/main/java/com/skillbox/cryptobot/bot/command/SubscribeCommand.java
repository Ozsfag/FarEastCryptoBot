package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.utils.MapperUtil.impl.MapperUtilImpl;
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
 * Обработка команды подписки на курс валюты
 */
@Service
@Lazy
@Slf4j
public class SubscribeCommand implements IBotCommand {
    private final GetPriceCommand getPriceCommand;
    private final CrudService crudService;
    private final MapperUtilImpl mapperUtil;
    private final MessageTextConfiguration messageTextConfiguration;

    public SubscribeCommand(GetPriceCommand getPriceCommand, CrudService crudService, MapperUtilImpl mapperUtil, MessageTextConfiguration messageTextConfiguration) {
        this.getPriceCommand = getPriceCommand;
        this.crudService = crudService;
        this.mapperUtil = mapperUtil;
        this.messageTextConfiguration = messageTextConfiguration.clone();
    }

    @Override
    public String getCommandIdentifier() {
        return messageTextConfiguration.getSubscribeCommandIdentifier();
    }

    @Override
    public String getDescription() {
        return messageTextConfiguration.getSubscribeCommandDescription();
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        getPriceCommand.processMessage(absSender, message, arguments);
        Double price = mapperUtil.getConvertedPrice(message);
        crudService.updateUser(message, price);
        String text = getText(price);

        SendMessage answer = SendMessageFactory.createSendMessage(message.getChatId(), text);

        executeAnswer(absSender, answer);
    }
    private String getText(Double price){
        return Objects.isNull(price) ?
                messageTextConfiguration.getSubscribeWrongInputMessage() :
                String.format(messageTextConfiguration.getSubscribeMessage(), price);
    }
    private void executeAnswer(AbsSender absSender, SendMessage answer){
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error(messageTextConfiguration.getSubscribeMessage(), e);
        }
    }
}
