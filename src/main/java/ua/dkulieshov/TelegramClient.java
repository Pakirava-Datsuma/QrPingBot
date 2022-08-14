package ua.dkulieshov;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import ua.dkulieshov.BotId.Cmd;
import ua.dkulieshov.BotId.Param;

public class TelegramClient {

  public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  final BotId botId;

  public TelegramClient(BotId botId) {
    this.botId = botId;
  }

  public Optional<String> getUpdates() {
    String url = botId.buildCmdUrl(Cmd.GET_UPDATES);
    return executeUrl(url);
  }

  private Optional<String> executeUrl(String url) {
    System.out.println();
    System.out.println(" < < < : " + botId.mask(url));
    HttpResponse<String> response = null;
    try {
      response = HTTP_CLIENT.send(HttpRequest.newBuilder().GET().uri(URI.create(url)).build(),
          responseInfo -> BodySubscribers.ofString(StandardCharsets.UTF_8));
      System.out.println(" > > > : " + botId.mask(response.body()));
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(response).map(HttpResponse::body);
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

  public void deleteMessage(String chatId, String messageId) {
    String url = botId.buildCmdUrl(Cmd.DELETE_MESSAGE,
        Map.of(Param.CHAT_ID, chatId, Param.MESSAGE_ID, messageId));
    Optional<String> maybeResponse = executeUrl(url);
    boolean notDeleted = maybeResponse.filter(response -> response.contains("true")).isEmpty();
    if (notDeleted) {
      System.out.println("\n! ! ! ! Not deleted ! ! !\n");
    }
  }

  public Optional<String> getMessageUpdates(String offset) {
    String url = botId.buildCmdUrl(Cmd.GET_UPDATES, Map.of(Param.OFFSET, offset));
    return executeUrl(url);
  }

  public Optional<String> send(String chatId, String message) {
    String url = botId.buildCmdUrl(Cmd.SEND_MESSAGE,
        Map.of(Param.CHAT_ID, chatId, Param.TEXT, message));
    return executeUrl(url);
  }

  public void updateMessage(String id, String repeatedMessage) {
    throw new RuntimeException("NotImplementedException");
  }
}
