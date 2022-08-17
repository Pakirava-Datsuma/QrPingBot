package ua.dkulieshov;

import com.google.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BotRunner {

  public static void main(String[] args) {
    BotId botId = new BotId(BotLoader.BOT_NAME, BotLoader.BOT_TOKEN);
    TelegramClient client = new TelegramClient(botId);
    Chat adminChat = new Chat(BotLoader.ADMIN_CHAT_ID, client);

    adminChat.sendQrCode(InputStream.nullInputStream());

    System.exit(0);

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

}
