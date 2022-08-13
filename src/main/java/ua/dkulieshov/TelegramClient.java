package ua.dkulieshov;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import ua.dkulieshov.TelegramBot.CmdPath;
import ua.dkulieshov.TelegramBot.ParamName;

public class TelegramClient {

  public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  public final String botCommandUrlPrefix;

  public TelegramClient(String botToken) {

    botCommandUrlPrefix = "https://api.telegram.org/bot" + botToken;
  }

  public String getUpdates() throws IOException, InterruptedException {
    return executeBotUrl(CmdPath.GET_UPDATES);
  }

  private String executeBotUrl(String command) throws IOException, InterruptedException {
    String url = getBotUrlOfSlashCommand(command);
    return executeUrl(url);
  }

  private String getBotUrlOfSlashCommand(String slashCommand) {
    String slash = "/";
    Preconditions.checkArgument(!botCommandUrlPrefix.endsWith(slash));
    Preconditions.checkArgument(slashCommand.startsWith(slash));
    return botCommandUrlPrefix + slashCommand;
  }

  private static String executeUrl(String url) throws IOException, InterruptedException {
    System.out.println();
    System.out.println(" < < < : " + url);
    HttpResponse<String> response = HTTP_CLIENT.send(
        HttpRequest.newBuilder().GET().uri(URI.create(url)).build(),
        responseInfo -> BodySubscribers.ofString(StandardCharsets.UTF_8));

    System.out.println(" > > > : " + response.toString());
    System.out.println(" > > > : " + response.body());

    return response.body();
  }

  public String getUpdates(String offset) throws IOException, InterruptedException {
    return executeBotUrl(CmdPath.GET_UPDATES, Map.of(ParamName.OFFSET, offset));
  }

  private String executeBotUrl(String command, Map<String, String> parameters)
      throws IOException, InterruptedException {
    return executeBotUrl(UrlUtils.buildUrl(command, parameters));
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
  public void send(String chatId, String message) throws IOException, InterruptedException {

    executeBotUrl(CmdPath.SEND_MESSAGE, Map.of(ParamName.CHAT_ID, chatId, ParamName.TEXT, message));
  }

}
