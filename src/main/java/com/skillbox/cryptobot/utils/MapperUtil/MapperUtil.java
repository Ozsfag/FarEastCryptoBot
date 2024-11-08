package com.skillbox.cryptobot.utils.MapperUtil;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MapperUtil {

  Double getConvertedPrice(Message message);
}
