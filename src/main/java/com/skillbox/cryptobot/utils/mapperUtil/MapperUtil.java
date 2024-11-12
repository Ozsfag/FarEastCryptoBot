package com.skillbox.cryptobot.utils.mapperUtil;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

public interface MapperUtil {

    Double getConvertedPrice(Message message);

    Optional<Double> getPrice();
}
