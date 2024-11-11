package com.skillbox.cryptobot.configuration;

import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pattern-configuration")
@NoArgsConstructor
@Getter
@Setter
public class PatternConfiguration implements Cloneable {
  private Collection<String> regexes;

  @Override
  public PatternConfiguration clone() {
    try {
      // TODO: copy mutable state here, so the clone can't change the internals of the original
      return (PatternConfiguration) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
