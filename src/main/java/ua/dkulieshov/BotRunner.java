package ua.dkulieshov;

import java.io.InputStream;
import java.util.List;

public class BotRunner {

  public static void main(String[] args) throws Exception {
    BotId botId = new BotId(BotLoader.BOT_NAME, BotLoader.BOT_TOKEN);
    TelegramClient client = new TelegramClient(botId);
    Chat adminChat = new Chat(BotLoader.ADMIN_CHAT_ID, client);

    sendQrCode(botId, adminChat); // TODO: send qr on "/start"

    String offset = Chat.waitFirstUpdate(client);
    adminChat.sendRepeatableMessage("Last offset found: " + offset);

    while (true) {
      offset = String.valueOf(Long.parseLong(offset) + 1);

      // TODO: get @from and:
      // TODO: 1. send "sent" back to @from
      // TODO: 2. send "@from" to chatId
      List<String> pingChatIds = Chat.waitPingChatIds(client, offset);

      for (String pingChatId : pingChatIds) {
        Chat chat = new Chat(pingChatId, client);
        chat.sendPing();
      }

      adminChat.sendRepeatableMessage("No!");
    }
  }

  private static void sendQrCode(BotId botId, Chat adminChat) throws Exception {
    String link = botId.buildPingLink(BotLoader.ADMIN_CHAT_ID);
    InputStream qrCodeImageStream = QrGenerator.generateQRCodeImage(link);
    adminChat.sendQrCode(qrCodeImageStream);
  }


}
