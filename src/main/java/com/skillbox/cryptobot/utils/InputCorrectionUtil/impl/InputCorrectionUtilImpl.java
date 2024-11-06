package com.skillbox.cryptobot.utils.InputCorrectionUtil.impl;

import com.skillbox.cryptobot.configuration.PatternConfiguration;
import com.skillbox.cryptobot.utils.InputCorrectionUtil.InputCorrectionUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class InputCorrectionUtilImpl implements InputCorrectionUtil {
    private final PatternConfiguration patternConfiguration;

    public InputCorrectionUtilImpl(PatternConfiguration patternConfiguration) {
        this.patternConfiguration = patternConfiguration;
    }

    @Override
    public String getMatchedInputText(String text) {
        return patternConfiguration.getRegexes().stream()
                .map(Pattern::compile)
                .map(pattern -> {
                    Matcher matcher = pattern.matcher(text);
                    return matcher.find() ?
                            matcher.group(1) : null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("");
    }
}
