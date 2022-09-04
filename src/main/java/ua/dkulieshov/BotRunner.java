package ua.dkulieshov;

import java.io.InputStream;
import java.util.List;

public class BotRunner {

  public static void main(String[] args) throws Exception {
    BotId botId = new BotId(BotLoader.BOT_NAME, BotLoader.BOT_TOKEN);
    TelegramClient client = new TelegramClient(botId);
    System.out.println(client.getMe());

    Chat adminChat = new Chat(BotLoader.ADMIN_CHAT_ID, client);

    sendQrCode(botId, adminChat); // TODO: send qr on "/start"

    String offset = Chat.waitFirstUpdate(client);
    adminChat.sendRepeatableMessage("Last offset found: " + offset);

    while (true) {
      offset = increment(offset);

      // TODO: get t.me/botName/start&startParam=chatId @from and:
      // TODO:    1. send "sent" back to @from
      // TODO:    2. send "@from" to chatId
      // TODO: show who is "@from" (do we have an API call to send AliceId to Bob if Bob knows Alice)
      // TODO: replace BOT_NAME print on start with t.me/getMe
      // TODO: on add to chat generate qr_code there
      List<ChatTask> chatTasks = Chat.waitChatTasks(client, offset);

      for (ChatTask task : chatTasks) {
        Chat chat = new Chat(task.chatId, client);
        if (ChatTask.SEND_PING.equals(task.action)) {
          chat.sendPing();
        } else {
          throw new RuntimeException("Unknown chat task: " + task.action);
        }
      }

      adminChat.sendRepeatableMessage("No!");
    }
  }

  private static String increment(String offset) {
    return String.valueOf(Long.parseLong(offset) + 1);
  }

  private static void sendQrCode(BotId botId, Chat adminChat) throws Exception {
    String link = botId.buildPingLink(BotLoader.ADMIN_CHAT_ID);
    InputStream qrCodeImageStream = QrGenerator.generateQRCodeImage(link);
    adminChat.sendQrCode(qrCodeImageStream);
  }
}
