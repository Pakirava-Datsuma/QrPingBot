package ua.dkulieshov;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

  public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  public static final String CHAT_ID_FILE = "chat.id";
  public static final String CHAT_ID = loadStringFromFile(CHAT_ID_FILE);
  public static final String BOT_TOKEN_FILE = "bot.token";
  public static final String BOT_TOKEN = loadStringFromFile(BOT_TOKEN_FILE);
  public static final String BOT_COMMAND_URL_PREFIX =
      String.format("https://api.telegram.org/bot%s", BOT_TOKEN);

  public static void main(String[] args) throws IOException, InterruptedException {
    String updates = execute("/getUpdates");

    execute("/sendMessage", Map.of("chat_id", CHAT_ID, "text", updates));
  }

  private static String encodeValue(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  private static String getBotUrlOfSlashCommand(String slashCommand) {
    String slash = "/";
    Preconditions.checkArgument(!BOT_COMMAND_URL_PREFIX.endsWith(slash));
    Preconditions.checkArgument(slashCommand.startsWith(slash));
    return BOT_COMMAND_URL_PREFIX + slashCommand;
  }

  private static String loadStringFromFile(String file) {
    try {
      return Resources.readLines(Resources.getResource(file), StandardCharsets.UTF_8).stream()
          .findFirst()
          .get();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String executeBotUrl(String command, Map<String, String> parameters)
      throws IOException, InterruptedException {
    String encodedParameters = encodeParametersSuffix(parameters);
    return executeBotUrl(command + encodedParameters);
  }

  private static String executeBotUrl(String command) throws IOException, InterruptedException {
    String url = getBotUrlOfSlashCommand(command);
    return executeUrl(url);
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

  private static String executeUrl(String url) throws IOException, InterruptedException {
    System.out.println();
    System.out.println(" < < < : " + url);
    HttpResponse<String> response =
        HTTP_CLIENT.send(
            HttpRequest.newBuilder().GET().uri(URI.create(url)).build(),
            responseInfo -> BodySubscribers.ofString(StandardCharsets.UTF_8));

    System.out.println(" > > > : " + response.toString());
    System.out.println(" > > > : " + response.body());

    return response.body();
  }
}
