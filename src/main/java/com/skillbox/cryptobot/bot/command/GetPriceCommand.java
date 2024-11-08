package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.configuration.MessageTextConfiguration;
import com.skillbox.cryptobot.service.cryptoCurrencyService.CryptoCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;

/**
 * Обработка команды получения текущей стоимости валюты
 */
@Service
@Lazy
@Slf4j
public class GetPriceCommand implements IBotCommand {

    private final CryptoCurrencyService service;
    private final MessageTextConfiguration messageTextConfiguration;

    public GetPriceCommand(CryptoCurrencyService service, MessageTextConfiguration messageTextConfiguration) {
        this.service = service;
        this.messageTextConfiguration = messageTextConfiguration.clone();
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
        Double price = getPrice();

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText(String.format(messageTextConfiguration.getGetPriceMessage(), price));

        executeAnswer(absSender, answer);
    }
    private Double getPrice() {
        //добавь обработку IOExceprion при отключенном интернете, это важно!!
        //должно возвращать сообщение об ошибке из application.yml
        Double price = null;
        try {
            price = service.getBitcoinPrice();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            return price;
        }
    }
    private void executeAnswer(AbsSender absSender, SendMessage answer){
        try {
            absSender.execute(answer);
        } catch (Exception e) {
            log.error(messageTextConfiguration.getGetPriceErrorMessage(), e);
        }
    }
}
