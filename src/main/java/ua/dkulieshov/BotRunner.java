package ua.dkulieshov;

import java.util.List;

public class BotRunner {

  public static void main(String[] args) throws Exception {
    BotId botId = new BotId(BotLoader.BOT_NAME, BotLoader.BOT_TOKEN);
    TelegramClient client = new TelegramClient(botId);
    System.out.println(client.getMe());

    Chat adminChat = new Chat(BotLoader.ADMIN_CHAT_ID, client);
    adminChat.sendSelfPingQrCode();

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
        if (ChatTask.SEND_PING.equals(task.action)) {
          Chat targetChat = new Chat(task.targetChatId, client);
          targetChat.sendPing();
          Chat sourceChat = new Chat(task.sourceChatId, client);
          // TODO: Why Reports do not stack?
          sourceChat.sendRepeatableMessage("Sent!");
        } else if (ChatTask.SEND_QR.equals(task.action)) {
          Chat chat = new Chat(task.targetChatId, client);
          chat.sendSelfPingQrCode();
        } else {
          throw new RuntimeException("Unknown chat task: " + task.action);
        }
      }

      adminChat.sendRepeatableMessage("Sent some ping");
    }
  }

  private static String increment(String offset) {
    return String.valueOf(Long.parseLong(offset) + 1);
  }
}
