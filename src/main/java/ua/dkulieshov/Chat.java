package ua.dkulieshov;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Chat {

  private final String chatId;
  private final TelegramClient client;
  private ChatMessageReference lastMessageSent = ChatMessageReference.EMPTY;
  private int lastMessageRepetitionNumber = 0;

  public Chat(String chatId, TelegramClient client) {
    this.chatId = chatId;
    this.client = client;
  }

  public static String waitFirstUpdate(TelegramClient client) {
    return waitUntilOptionalIsPresent(() -> client
        .getUpdates()
        .flatMap(TelegramParser::parseUpdateOffset));
  }

  private static <T> T waitUntilOptionalIsPresent(Supplier<Optional<T>> responder) {
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

  public static List<String> waitUpdateChatIds(TelegramClient client, String offset) {
    //@formatter:off
    List<String> chatIds = waitUntilOptionalIsPresent(() -> client
        .getMessageUpdates(offset)
        .map(TelegramParser::parseChatIds)
        .filter(list -> !list.isEmpty()));
    //@formatter:on
    return chatIds;
  }

  public static List<ChatTask> waitChatTasks(TelegramClient client, String offset) {
    //@formatter:off
    List<ChatTask> chatTasks = waitUntilOptionalIsPresent(() -> client
        .getMessageUpdates(offset)
        .map(TelegramParser::parseChatTasks)
        .filter(list -> !list.isEmpty()));
    //@formatter:on
    return chatTasks;
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
    String link = client.botId.buildStartLinkWithKeyNGroup(chatId, chatId);
    String text = String.format("Hey! This chat id = %s\n%s", chatId, link);
    Optional<String> maybeResponseWithMessageId = client.send(chatId, text);
    maybeResponseWithMessageId.flatMap(TelegramParser::parseSentMessageId).ifPresent(id -> {
      lastMessageSent = new ChatMessageReference(id, text);
      lastMessageRepetitionNumber = 1;
    });
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

  public void sendRepeatableMessage(String text) {
    boolean toResend = lastMessageSent.textEquals(text);
    if (toResend) {
      client.deleteMessage(chatId, lastMessageSent.getId());
      int repetitionNumber = lastMessageRepetitionNumber + 1;
      String repeatedMessage = "(" + repetitionNumber + ") " + text;
      Optional<String> maybeMessageId =
          client.send(chatId, repeatedMessage).flatMap(TelegramParser::parseSentMessageId);
      if (maybeMessageId.isPresent()) {
        String id = maybeMessageId.get();
        lastMessageSent = new ChatMessageReference(id, text);
        lastMessageRepetitionNumber = repetitionNumber;
      }
    } else {
      int repetitionNumber = 1;
      Optional<String> maybeMessageId =
          client.send(chatId, text).flatMap(TelegramParser::parseSentMessageId);
      if (maybeMessageId.isPresent()) {
        String id = maybeMessageId.get();
        lastMessageSent = new ChatMessageReference(id, text);
        lastMessageRepetitionNumber = repetitionNumber;
      }
    }
  }

  public void sendQrCode(InputStream inputStream) {
    String filename = String.format("QR ping code for chat %s.png", chatId);
    client.sendPhoto(chatId, inputStream, filename);
  }

  public void sendPing() {
    client.send(chatId, "ping"/* from: @zovitemenysemero */);
  }
}
