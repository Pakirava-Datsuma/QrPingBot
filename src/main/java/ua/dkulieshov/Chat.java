package ua.dkulieshov;

import java.io.IOException;
import java.util.Map;

public class Chat {

  private final String chatId;
  private final TelegramClient client;
  private final TelegramBot bot;
  private TelegramParser parser;

  public Chat(String chatId, TelegramClient client, TelegramBot bot) {
    this.chatId = chatId;
    this.client = client;
    this.bot = bot;
  }

  /*{
    "ok": true,
    "result": [
      {
        "update_id": 713697435,
        "message": {
          "message_id": 576,
          "from": {"id": 221287654, "is_bot": false, "first_name": "Dm\u0443troK", "last_name": "\ud83c\uddfa\ud83c\udde6", "username": "Pakirava_Datsuma", "language_code": "uk"},
          "chat": {"id": 221287654, "first_name": "Dm\u0443troK", "last_name": "\ud83c\uddfa\ud83c\udde6", "username": "Pakirava_Datsuma", "type": "private"},
          "date": 1658613784,
          "text": "/start PingPrivate",
          "entities": [{"offset": 0, "length": 6, "type": "bot_command"}]
        }
      }
    ]
  }*/
  public void sendLinkToStartBot() throws IOException, InterruptedException {
    String link = bot.buildStartLinkWithKey();
    client.send(chatId, link);
  }

  /*{
    "ok": true,
    "result": [
      {
        "update_id": 713697435,
        "message": {
          "message_id": 3,
          "from": {"id": 221287654, "is_bot": false, "first_name": "Dm\u0443troK", "last_name": "\ud83c\uddfa\ud83c\udde6", "username": "Pakirava_Datsuma", "language_code": "uk"},
          "chat": {"id": -1001650852901, "title": "dv-ftp", "type": "supergroup"},
          "date": 1658613231,
          "text": "/start@swantabot PingGroup",
          "entities": [{"offset": 0, "length": 16, "type": "bot_command"}]
        }
      }
    ]
  }
  */
  public void sendLinkToInviteBotIntoGroup(String inviteGroup)
      throws IOException, InterruptedException {
    String startBotInGroupWithParameter = client.buildBotStartForGroup(inviteGroup);

    client.send(chatId, startBotInGroupWithParameter, client.buildBotStartForGroup(chatIdOrName));
  }

  /*{
    "ok": true,
    "result": [
      {
        "update_id": 713697435,
        "message": {
          "message_id": 576,
          "from": {"id": 221287654, "is_bot": false, "first_name": "Dm\u0443troK", "last_name": "\ud83c\uddfa\ud83c\udde6", "username": "Pakirava_Datsuma", "language_code": "uk"},
          "chat": {"id": 221287654, "first_name": "Dm\u0443troK", "last_name": "\ud83c\uddfa\ud83c\udde6", "username": "Pakirava_Datsuma", "type": "private"},
          "date": 1658613784,
          "text": "/start PingPrivate",
          "entities": [{"offset": 0, "length": 6, "type": "bot_command"}]
        }
      }
    ]
  }*/
  public void sendLinkToStartBot(String adminChatId) throws IOException, InterruptedException {
    String startBotPrivateWithParameter =
        selfLinkPrefix + buildParametersSuffix(Map.of(START_PARAM_NAME, "PingPrivate"));

    client.send(adminChatId, startBotPrivateWithParameter);
  }

}
