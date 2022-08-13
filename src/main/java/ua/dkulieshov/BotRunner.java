package ua.dkulieshov;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BotRunner {


  public static final String CHAT_ID_FILE = "chat.id";
  public static final String CHAT_ID = loadStringFromFile(CHAT_ID_FILE);
  public static final String BOT_NAME_FILE = "bot.name";
  public static final String BOT_NAME = loadStringFromFile(BOT_NAME_FILE);

  public static final String BOT_TOKEN_FILE = "bot.token";
  public static final String BOT_TOKEN = loadStringFromFile(BOT_TOKEN_FILE);


  public static void main(String[] args) throws InterruptedException {
    TelegramClient client = new TelegramClient(new TelegramBot(BOT_NAME, BOT_TOKEN));

    String offset = Chat.waitFirstUpdate(client);
    Chat.waitNextUpdates(client, offset).forEach(chat -> chat.sendLinkToStartBot());

  }


  private static String loadStringFromFile(String file) {
    try {
      return Resources.readLines(Resources.getResource(file), StandardCharsets.UTF_8).stream()
          .findFirst().get();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
