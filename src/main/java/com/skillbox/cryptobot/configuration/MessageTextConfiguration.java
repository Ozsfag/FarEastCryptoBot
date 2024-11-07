package com.skillbox.cryptobot.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pattern-configuration")
@NoArgsConstructor
public class MessageTextConfiguration {
    private String startGreetingMessage;
    private String startErrorMessage;
    private String getPriceMessage;
    private String subscribeWrongInputMessage;
    private String subscribeMessage;
    private String subscribeErrorMessage;
    private String getNonActiveSubscriptionMessage;
    private String getSubscriptionMessage;
    private String getSubscriptionErrorMessage;
    private String unsubscribeMessage;
    private String unsuscribeErrorMessage;

    public void setStartGreetingMessage(String startGreetingMessage) {
        this.startGreetingMessage = String.copyValueOf(startGreetingMessage.toCharArray());
    }

    public void setStartErrorMessage(String startErrorMessage) {
        this.startErrorMessage = startErrorMessage;
    }

    public void setGetPriceMessage(String getPriceMessage) {
        this.getPriceMessage = getPriceMessage;
    }

    public void setSubscribeWrongInputMessage(String subscribeWrongInputMessage) {
        this.subscribeWrongInputMessage = subscribeWrongInputMessage;
    }

    public void setSubscribeMessage(String subscribeMessage) {
        this.subscribeMessage = subscribeMessage;
    }

    public void setSubscribeErrorMessage(String subscribeErrorMessage) {
        this.subscribeErrorMessage = subscribeErrorMessage;
    }

    public void setGetNonActiveSubscriptionMessage(String getNonActiveSubscriptionMessage) {
        this.getNonActiveSubscriptionMessage = getNonActiveSubscriptionMessage;
    }

    public void setGetSubscriptionMessage(String getSubscriptionMessage) {
        this.getSubscriptionMessage = getSubscriptionMessage;
    }

    public void setGetSubscriptionErrorMessage(String getSubscriptionErrorMessage) {
        this.getSubscriptionErrorMessage = getSubscriptionErrorMessage;
    }

    public void setUnsubscribeMessage(String unsubscribeMessage) {
        this.unsubscribeMessage = unsubscribeMessage;
    }

    public void setUnsuscribeErrorMessage(String unsuscribeErrorMessage) {
        this.unsuscribeErrorMessage = unsuscribeErrorMessage;
    }
    @PostConstruct
    private void init(){

    }
}
