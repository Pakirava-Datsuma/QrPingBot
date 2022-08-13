package ua.dkulieshov;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class BotRunner {


  public static final String CHAT_ID_FILE = "chat.id";
  public static final String CHAT_ID = loadStringFromFile(CHAT_ID_FILE);
  public static final String BOT_NAME_FILE = "bot.name";
  public static final String BOT_NAME = loadStringFromFile(BOT_NAME_FILE);

  public static final String BOT_TOKEN_FILE = "bot.token";
  public static final String BOT_TOKEN = loadStringFromFile(BOT_TOKEN_FILE);


  public static void main(String[] args) throws InterruptedException {
    TelegramBot bot = new TelegramBot(BOT_NAME, BOT_TOKEN);
    TelegramClient client = new TelegramClient(bot);

    String offset = waitFirstUpdate(client);
    waitNextUpdates(client, offset);
    Chat chat = new Chat(CHAT_ID, client, bot);
  }

  private static String waitFirstUpdate(TelegramClient client) throws InterruptedException {
    Optional<String> maybeInitialOffset = client.getUpdates()
        .map(TelegramParser::parseUpdateOffset);
    while (maybeInitialOffset.isEmpty()) {
      Thread.sleep(500);
    }
    return maybeInitialOffset.get();
  }

  private static Chat[] waitNextUpdates(TelegramClient client, String offset) {
    Optional<String> maybeInitialOffset = client.getUpdates(offset + 1)
        .map(TelegramParser::parseUpdateOffset);
    while (maybeInitialOffset.isEmpty()) {
      Thread.sleep(500);
    }
    return maybeInitialOffset.get();
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
