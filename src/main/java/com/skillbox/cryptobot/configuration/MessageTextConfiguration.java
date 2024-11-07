package com.skillbox.cryptobot.configuration;

import com.skillbox.cryptobot.annotations.ImmutableDataMapper;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pattern-configuration")
@NoArgsConstructor
public class MessageTextConfiguration {
    @ImmutableDataMapper
    private String startGreetingMessage;
    @ImmutableDataMapper
    private String startErrorMessage;
    @ImmutableDataMapper
    private String getPriceMessage;
    @ImmutableDataMapper
    private String subscribeWrongInputMessage;
    @ImmutableDataMapper
    private String subscribeMessage;
    @ImmutableDataMapper
    private String subscribeErrorMessage;
    @ImmutableDataMapper
    private String getNonActiveSubscriptionMessage;
    @ImmutableDataMapper
    private String getSubscriptionMessage;
    @ImmutableDataMapper
    private String getSubscriptionErrorMessage;
    @ImmutableDataMapper
    private String unsubscribeMessage;
    @ImmutableDataMapper
    private String unsuscribeErrorMessage;
}
