package ua.dkulieshov;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

public class BotRunner {

  public static void main(String[] args) throws Exception {
    BotId botId = new BotId(BotLoader.BOT_NAME, BotLoader.BOT_TOKEN);
    TelegramClient client = new TelegramClient(botId);
    Chat adminChat = new Chat(BotLoader.ADMIN_CHAT_ID, client);

    String link = botId.buildInviteLink();
    InputStream qrCodeImageStream = QrGenerator.generateQRCodeImage(link);
    adminChat.sendQrCode(qrCodeImageStream);

    System.exit(0);

    String offset = Chat.waitFirstUpdate(client);
    adminChat.sendRepeatableMessage("Last offset found: " + offset);

    while (true) {
      offset = String.valueOf(Long.parseLong(offset) + 1);

      List<String> chatIds = Chat.waitUpdateChatIds(client, offset);

      chatIds.forEach(id -> answerToChat(id));

      adminChat.sendRepeatableMessage("No!");
    }
  }

  private static void answerToChat(String id) {

  }

}
