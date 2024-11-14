package com.skillbox.cryptobot.utils.mapperUtil;

import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MapperUtil {

  Double getConvertedFromMessageBitcoinPrice(Message message);

  Optional<Double> getCurrentBitcoinPrice();
}
