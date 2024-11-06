package com.skillbox.cryptobot.utils.MapperUtil;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MapperUtil {
    String toString(double value);

    Double getConvertedPrice(Message message);
}
