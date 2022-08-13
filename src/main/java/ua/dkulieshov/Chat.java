package ua.dkulieshov;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Chat {

  private final String chatId;
  private final TelegramClient client;
  private TelegramParser parser;

  public Chat(String chatId, TelegramClient client) {
    this.chatId = chatId;
    this.client = client;
  }

  public static String waitFirstUpdate(TelegramClient client) {
    String responseWithUpdateOffset = waitUntil(client::getUpdates);
    return TelegramParser.parseUpdateOffset(responseWithUpdateOffset);
  }

  private static <T> T waitUntil(Supplier<Optional<T>> responder) {
    Optional<T> maybeValue = Optional.empty();
    while (maybeValue.isEmpty()) {
      maybeValue = responder.get();
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        System.out.println("Thread is interrupted");
      }
    }
    return maybeValue.get();
  }

  public static Stream<Chat> waitNextUpdates(TelegramClient client, String offset) {
    String nextOffset = String.valueOf(Long.parseLong(offset) + 1);
    String responseWithChatMessages = waitUntil(() -> client.getMessageUpdates(nextOffset));
    Stream<String> chatIds = TelegramParser.parseChatIds(responseWithChatMessages);
    return chatIds.map(id -> new Chat(id, client));
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
  public void sendLinkToStartBot() {
    String link = client.bot.buildStartLinkWithKeyNGroup(chatId, chatId);
    String message = String.format("Hey! This chat id = %s\n%s", chatId, link);
    client.send(chatId, message);
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
/*
    String startBotInGroupWithParameter = client.buildBotStartForGroup(inviteGroup);

    client.send(chatId, startBotInGroupWithParameter, client.buildBotStartForGroup(chatIdOrName));
*/
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
/*
    String startBotPrivateWithParameter =
        selfLinkPrefix + buildParametersSuffix(Map.of(START_PARAM_NAME, "PingPrivate"));

    client.send(adminChatId, startBotPrivateWithParameter);
*/
  }

}
