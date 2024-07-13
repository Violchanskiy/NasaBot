package org.example;

import classes.MyBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
  public static void main(String[] args) {
    String nameBot = "Violchanskiys_nasa_bot";
    String tokenBot = "7185439026:AAGAuQ9TpvqqNlovMk-R4vMlGarekdx4p_E";
    try {
      new MyBot(nameBot, tokenBot);
    } catch (TelegramApiException e) {
      System.out.println(e.getMessage());
    }
  }
}
