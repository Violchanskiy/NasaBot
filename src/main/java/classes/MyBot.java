package classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MyBot extends TelegramLongPollingBot {
  private final String name;
  private final String token;
  private final String helloText =
      "Привет, я бот, который отправляет астрономическую картину дня от Nasa.\n"
          + "Чтобы узнать, что я могу, используйте команду /help";
  private final String helpText =
      "/picture - получение картинки дня\n"
          + "/date - получение картинки дня по определенной дате\n"
          + "/help - помощь\n";

  private long chatId;

  public MyBot(String name, String token) throws TelegramApiException {
    this.name = name;
    this.token = token;
    TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
    botsApi.registerBot(this);
  }

  @Override
  public String getBotUsername() {
    return name;
  }

  @Override
  public String getBotToken() {
    return token;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      chatId = update.getMessage().getChatId();
      String text = update.getMessage().getText();
      switch (text) {
        case "/start":
          sendMessage(helloText);
          break;
        case "/help":
          sendMessage(helpText);
          break;
        case "/picture":
          sendPicture();
          break;
        case "/date":
          sendMessage("Введите дату в формате YYYY-MM-DD");
          break;
        default:
          if (isDateValid(text)) {
            LocalDate currentDate = LocalDate.now();
            LocalDate dateToCheck = LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
            if (dateToCheck.equals(currentDate)) {
              sendPicture();
            } else {
              sendPicture(text);
            }
          } else {
            sendMessage("Некорректный ввод");
          }
      }
    }
  }

  private void sendPicture() {
    SendPhoto photo = new SendPhoto();
    photo.setChatId(chatId);
    photo.setPhoto(new InputFile(new Utils().getUrl()));
    startSend(photo);
  }

  private void sendPicture(String date) {
    SendPhoto photo = new SendPhoto();
    photo.setChatId(chatId);
    String url = new Utils().getUrl(date);
    if (url != null) {
      photo.setPhoto(new InputFile(new Utils().getUrl(date)));
      startSend(photo);
    } else {
      sendMessage("Некорректный ввод");
    }
  }

  private void startSend(SendPhoto photo) {
    try {
      execute(photo);
    } catch (TelegramApiException e) {
      System.out.println(e.getMessage());
    }
  }

  private boolean isDateValid(String date) {
    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
    myFormat.setLenient(false);
    try {
      myFormat.parse(date);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  private void sendMessage(String text) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(text);
    try {
      execute(message);
    } catch (TelegramApiException e) {
      System.out.println(e.getMessage());
    }
  }
}
