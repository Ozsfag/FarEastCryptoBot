package com.skillbox.cryptobot.utils.mapperUtil.impl;

import com.skillbox.cryptobot.service.cryptoCurrencyService.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.inputCorrectionUtil.InputCorrectionUtil;
import com.skillbox.cryptobot.utils.mapperUtil.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class MapperUtilImpl implements MapperUtil {
    private final InputCorrectionUtil inputCorrectionUtil;
    private final CryptoCurrencyService service;

    public MapperUtilImpl(InputCorrectionUtil inputCorrectionUtil, CryptoCurrencyService service) {
        this.inputCorrectionUtil = inputCorrectionUtil;
        this.service = service;
    }

    @Override
    public Double getConvertedPrice(Message message) {
        if (message.hasText()) {
            String price = inputCorrectionUtil.getMatchedInputText(message.getText());
            try {
                return Double.valueOf(price);
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        throw new UnsupportedOperationException("Вы не ввели текст");
    }

    @Override
    public Optional<Double> getPrice() {
        try {
            double price = service.getBitcoinPrice();
            log.info("Successfully retrieved Bitcoin price: {}", price);
            return Optional.of(price);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
