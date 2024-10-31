package com.skillbox.cryptobot.utils;

import org.telegram.telegrambots.meta.api.objects.Message;

public class MapperUtil {

  public static String toString(double value) {
    return String.format("%.3f", value);
  }

  public static Double getConvertedPrice(Message message) {
    String mes = message.getText();
    int start = mes.indexOf('[') + 1;
    int end = mes.lastIndexOf(']');
    return Double.valueOf(mes.substring(start, end));
  }
}
