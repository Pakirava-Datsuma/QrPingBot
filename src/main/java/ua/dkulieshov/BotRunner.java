package ua.dkulieshov;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BotRunner {


  public static final String ADMIN_CHAT_ID_FILE = "chat.id";
  public static final String ADMIN_CHAT_ID = loadStringFromFile(ADMIN_CHAT_ID_FILE);
  public static final String BOT_NAME_FILE = "bot.name";
  public static final String BOT_NAME = loadStringFromFile(BOT_NAME_FILE);

  public static final String BOT_TOKEN_FILE = "bot.token";
  public static final String BOT_TOKEN = loadStringFromFile(BOT_TOKEN_FILE);


  public static void main(String[] args) {
    TelegramClient client = new TelegramClient(new Bot(BOT_NAME, BOT_TOKEN));
    Chat adminChat = new Chat(ADMIN_CHAT_ID, client);

    String offset = Chat.waitFirstUpdate(client);
    adminChat.sendRepeatableMessage("Last offset found: " + offset);

    while (true) {
      offset = String.valueOf(Long.parseLong(offset) + 1);

      List<String> chatIds = Chat.waitUpdateChatIds(client, offset);

      chatIds.forEach(id -> answerToChat(id));

      /*

Exception in thread "main" java.lang.RuntimeException: NotImplementedException
	at ua.dkulieshov.TelegramClient.updateMessage(TelegramClient.java:75)
	at ua.dkulieshov.Chat.sendRepeatableMessage(Chat.java:131)
	at ua.dkulieshov.BotRunner.main(BotRunner.java:34)
       */
      adminChat.sendRepeatableMessage("No!");
    }
  }

  private static void answerToChat(String id) {

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
