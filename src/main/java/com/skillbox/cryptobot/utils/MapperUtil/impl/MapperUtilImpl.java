package com.skillbox.cryptobot.utils.MapperUtil.impl;

import com.skillbox.cryptobot.utils.InputCorrectionUtil.InputCorrectionUtil;
import com.skillbox.cryptobot.utils.MapperUtil.MapperUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MapperUtilImpl implements MapperUtil {
  private final InputCorrectionUtil inputCorrectionUtil;

  public MapperUtilImpl(InputCorrectionUtil inputCorrectionUtil) {
    this.inputCorrectionUtil = inputCorrectionUtil;
  }

  @Override
  public String toString(double value) {
    return String.format("%.3f", value);
  }

  @Override
  public Double getConvertedPrice(Message message) {
    String price = inputCorrectionUtil.getMatchedInputText(message.getText());
    try {
      return Double.valueOf(price);
    } catch (NumberFormatException nfe) {
      return null;
    }
  }
}
