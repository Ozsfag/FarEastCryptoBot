package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.utils.MapperUtil.impl.MapperUtilImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {
    private final GetPriceCommand getPriceCommand;
    private final CrudService crudService;
    private final MapperUtilImpl mapperUtil;

    public SubscribeCommand(GetPriceCommand getPriceCommand, CrudService crudService, MapperUtilImpl mapperUtil) {
        this.getPriceCommand = getPriceCommand;
        this.crudService = crudService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        getPriceCommand.processMessage(absSender, message, arguments);
        Double price = mapperUtil.getConvertedPrice(message);
        crudService.updateUser(message, price);

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        String text = Objects.isNull(price) ?
                "Неверный формат ввода" :
                "Новая подписка создана на стоимость " + price + " USD";

        answer.setText(text);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }
}
