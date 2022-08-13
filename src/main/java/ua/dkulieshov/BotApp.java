package ua.dkulieshov;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class BotApp {


  public static final String BOT_NAME_FILE = "bot.name";
  public static final String BOT_NAME = loadStringFromFile(BOT_NAME_FILE);

  public static final String BOT_TOKEN_FILE = "bot.token";
  public static final String BOT_TOKEN = loadStringFromFile(BOT_TOKEN_FILE);


  public static void main(String[] args) throws IOException, InterruptedException {
    TelegramClient client = new TelegramClient();
    String updatesResponse = client.getUpdates();
    Optional<String> maybeOffset = telegramParser.parseUpdateOffset(updatesResponse);

    client.sendLinkToInviteBotIntoGroup();

    client.sendLinkToStartBot();
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
