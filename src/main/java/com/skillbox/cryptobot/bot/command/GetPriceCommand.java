package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.factory.SendMessageFactory;
import com.skillbox.cryptobot.utils.answerExecutor.AnswerExecutor;
import com.skillbox.cryptobot.utils.mapperUtil.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

/**
 * Обработка команды получения текущей стоимости валюты
 */
@Service
@Lazy
@Slf4j
public class GetPriceCommand implements IBotCommand {
    private final MessageTextConfiguration messageTextConfiguration;
    private final MapperUtil mapperUtil;
    private final SendMessageFactory sendMessageFactory;

    public GetPriceCommand(
            MessageTextConfiguration messageTextConfiguration, MapperUtil mapperUtil, SendMessageFactory sendMessageFactory) {
        this.messageTextConfiguration = messageTextConfiguration.clone();
        this.mapperUtil = mapperUtil;
        this.sendMessageFactory = sendMessageFactory;
    }

    @Override
    public String getCommandIdentifier() {
        return messageTextConfiguration.getGetPriceCommandIdentifier();
    }

    @Override
    public String getDescription() {
        return messageTextConfiguration.getGetPriceCommandDescription();
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        Optional<Double> priceOptional = mapperUtil.getPrice();

        String responseText = priceOptional
                .map(price -> String.format(messageTextConfiguration.getGetPriceMessage(), price))
                .orElse(messageTextConfiguration.getGetPriceDisconnectMessage());

        SendMessage answer = sendMessageFactory.createSendMessage(message.getChatId(), responseText);
        AnswerExecutor.executeAnswer(absSender, answer, answer.getText());

    }

}
