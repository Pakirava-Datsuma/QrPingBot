package ua.dkulieshov;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BotLoader {

  public static final String ADMIN_CHAT_ID_FILE = "chat.id";
  public static final String ADMIN_CHAT_ID = loadStringFromFile(ADMIN_CHAT_ID_FILE);
  public static final String BOT_NAME_FILE = "bot.name";
  public static final String BOT_NAME = loadStringFromFile(BOT_NAME_FILE);

  public static final String BOT_TOKEN_FILE = "bot.token";
  public static final String BOT_TOKEN = loadStringFromFile(BOT_TOKEN_FILE);

  private static String loadStringFromFile(String file) {
    try {
      return Resources
          .readLines(Resources.getResource(file), StandardCharsets.UTF_8)
          .stream()
          .findFirst()
          .get();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void printBotConfig() {
    System.out.println("BOT_NAME: " + BOT_NAME);
    System.out.println("ADMIN_CHAT_ID: " + ADMIN_CHAT_ID);
  }
}
