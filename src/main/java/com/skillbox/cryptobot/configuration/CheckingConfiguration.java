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
    private Integer checkingDelay;
    private Integer checkingFrequency;
    private Integer notificationDelay;
    private Integer notificationFrequency;


    @Override
    public CheckingConfiguration clone() {
        try {
            return (CheckingConfiguration) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
