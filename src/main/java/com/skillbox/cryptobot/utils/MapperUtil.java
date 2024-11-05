package com.skillbox.cryptobot.utils;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapperUtil {

  public static String toString(double value) {
    return String.format("%.3f", value);
  }

  public static Double getConvertedPrice(Message message) {
    Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
    Matcher matcher = pattern.matcher(message.getText());
    if (matcher.find()) {
      String number = matcher.group(1);
      return Double.valueOf(number);
    }

  }
}
