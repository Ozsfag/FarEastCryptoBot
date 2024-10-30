package com.skillbox.cryptobot.service.cryptoCurrencyService.impl;

import com.skillbox.cryptobot.client.BinanceClient;
import com.skillbox.cryptobot.service.cryptoCurrencyService.CryptoCurrencyService;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client;

    public CryptoCurrencyServiceImpl(BinanceClient client) {
        this.client = client;
    }

    @Override
    public double getBitcoinPrice() throws IOException {
        if (price.get() == null) {
            price.set(client.getBitcoinPrice());
        }
        return price.get();
    }
}
