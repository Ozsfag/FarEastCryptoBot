package com.skillbox.cryptobot.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "message-text-configuration")
@NoArgsConstructor(force = true)
@Setter
@Getter
public class MessageTextConfiguration implements Cloneable {
  private String startCommandIdentifier;
  private String startCommandDescription;
  private String startGreetingsMessage;
  private String startErrorMessage;
  private String getPriceCommandIdentifier;
  private String getPriceCommandDescription;
  private String getPriceMessage;
  private String getPriceErrorMessage;
  private String subscribeCommandIdentifier;
  private String subscribeCommandDescription;
  private String subscribeWrongInputMessage;
  private String subscribeMessage;
  private String subscribeErrorMessage;
  private String getSubscriptionCommandIdentifier;
  private String getSubscriptionCommandDescription;
  private String getNonActiveSubscriptionMessage;
  private String getSubscriptionMessage;
  private String getSubscriptionErrorMessage;
  private String unsubscribeMessage;
  private String unsubscribeErrorMessage;
  private String checkNotification;

  @Override
  public MessageTextConfiguration clone() {
    try {
      // TODO: copy mutable state here, so the clone can't change the internals of the original
      return (MessageTextConfiguration) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
