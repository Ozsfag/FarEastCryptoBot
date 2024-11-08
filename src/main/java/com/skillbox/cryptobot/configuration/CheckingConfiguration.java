package com.skillbox.cryptobot.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "checking-configuration")
@NoArgsConstructor(force = true)
@Setter
@Getter
public class CheckingConfiguration implements Cloneable {
  private Integer checkingFrequency;
  private Integer notificationFrequency;
  private Integer delay;

  @Override
  public CheckingConfiguration clone() {
    try {
      // TODO: copy mutable state here, so the clone can't change the internals of the original
      return (CheckingConfiguration) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}