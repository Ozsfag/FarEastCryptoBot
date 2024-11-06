package com.skillbox.cryptobot.configuration;

import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
@ConfigurationProperties(prefix = "pattern-configuration")
@NoArgsConstructor
public class PatternConfiguration {
    private Collection<String> regexes;

    public void setRegexes(Collection<String> regexes) {
        this.regexes = Collections.unmodifiableCollection(regexes);
    }

    public Collection<String> getRegexes() {
        return Collections.unmodifiableCollection(regexes);
    }
}
