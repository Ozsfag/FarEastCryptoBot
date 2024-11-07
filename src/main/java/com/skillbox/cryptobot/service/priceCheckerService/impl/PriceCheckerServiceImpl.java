package com.skillbox.cryptobot.service.priceCheckerService.impl;

import com.skillbox.cryptobot.service.crudService.CrudService;
import com.skillbox.cryptobot.service.cryptoCurrencyService.CryptoCurrencyService;
import com.skillbox.cryptobot.service.priceCheckerService.PriceCheckerService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.concurrent.*;

@Component
public class PriceCheckerServiceImpl implements PriceCheckerService {
    private final CryptoCurrencyService cryptoCurrencyService;
    private final CrudService crudService;

    public PriceCheckerServiceImpl(CryptoCurrencyService cryptoCurrencyService, CrudService crudService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.crudService = crudService;
    }

    public void doCheck(Message message) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Callable<?> task = () -> {
            try {
                return cryptoCurrencyService.getBitcoinPrice();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        ScheduledFuture<?> future = executorService.schedule(task, 2, TimeUnit.MINUTES);

        try (executorService) {
            Double currentPrice = (Double) future.get(10, TimeUnit.MINUTES);
            Double subscriptionPrice = crudService.getPrice(message);
            if (currentPrice >= subscriptionPrice) {

            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
