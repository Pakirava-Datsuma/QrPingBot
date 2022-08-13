package ua.dkulieshov;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class TelegramClient {

  public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

  public static final String CHAT_ID_FILE = "chat.id";
  public static final String CHAT_ID = loadStringFromFile(CHAT_ID_FILE);

  public static final String BOT_NAME_FILE = "bot.name";
  public static final String BOT_NAME = loadStringFromFile(BOT_NAME_FILE);
  public static final String USER_LINK_PREFIX = "https://t.me/" + BOT_NAME;

  public static final String BOT_TOKEN_FILE = "bot.token";
  public static final String BOT_TOKEN = loadStringFromFile(BOT_TOKEN_FILE);

  public static final String BOT_COMMAND_URL_PREFIX = "https://api.telegram.org/bot" + BOT_TOKEN;

  private static String loadStringFromFile(String file) {
    try {
      return Resources.readLines(Resources.getResource(file), StandardCharsets.UTF_8).stream()
          .findFirst().get();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getUpdates() throws IOException, InterruptedException {
    return executeBotUrl("/getUpdates");
  }

  public String getUpdates(String offset) throws IOException, InterruptedException {
    return executeBotUrl("/getUpdates", Map.of("offset", String.valueOf(offset)));
  }

  private String executeBotUrl(String command) throws IOException, InterruptedException {
    String url = getBotUrlOfSlashCommand(command);
    return executeUrl(url);
  }

  private static String getBotUrlOfSlashCommand(String slashCommand) {
    String slash = "/";
    Preconditions.checkArgument(!BOT_COMMAND_URL_PREFIX.endsWith(slash));
    Preconditions.checkArgument(slashCommand.startsWith(slash));
    return BOT_COMMAND_URL_PREFIX + slashCommand;
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
    String startBotPrivateWithParameter =
        USER_LINK_PREFIX + encodeParametersSuffix(Map.of("start", "PingPrivate"));

    executeBotUrl("/sendMessage", Map.of("chat_id", CHAT_ID, "text", startBotPrivateWithParameter));
  }

  private static String encodeParametersSuffix(Map<String, String> parameters) {
    String parametersPrefix = "?";
    String keyValueDelimiter = "=";
    String keyValuePairsDelimiter = "&";
    String parametersSuffix = "";

    return parameters.keySet().stream()
        .map(key -> key + keyValueDelimiter + encodeValue(parameters.get(key)))
        .collect(Collectors.joining(keyValuePairsDelimiter, parametersPrefix, parametersSuffix));
  }

  private String executeBotUrl(String command, Map<String, String> parameters)
      throws IOException, InterruptedException {
    String encodedParameters = encodeParametersSuffix(parameters);
    return executeBotUrl(command + encodedParameters);
  }

  private static String encodeValue(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
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
  public void sendLinkToInviteBotIntoGroup() throws IOException, InterruptedException {
    String startBotInGroupWithParameter =
        USER_LINK_PREFIX + encodeParametersSuffix(Map.of("startgroup", "PingGroup"));

    executeBotUrl("/sendMessage", Map.of("chat_id", CHAT_ID, "text", startBotInGroupWithParameter));
  }
}
