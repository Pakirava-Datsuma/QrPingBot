package ua.dkulieshov;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TelegramParser {

  public static final String DELIMITER = "%";
  public static final String WRAP_1_WITH_DELIMITERS = DELIMITER + "$1" + DELIMITER;
  private static ObjectMapper objectMapper = new ObjectMapper();

  public static Optional<String> parseUpdateOffset(String response) {
    return extractFirstRegexGroup(response, RegexPattern.UPDATE_ID);
  }

  private static Optional<String> extractFirstRegexGroup(String response, RegexPattern regex) {
    String[] parts = response.replaceAll(regex.regex, WRAP_1_WITH_DELIMITERS).split(DELIMITER);
    Optional<String> maybeSecondPart =
        Optional.of(parts).filter(array -> array.length > 1).map(array -> array[1]);

    if (maybeSecondPart.isEmpty()) {
      System.out.println("Not found first group. Parts: " + Arrays.toString(parts));
    }

    return maybeSecondPart;
  }

  public static List<String> parseChatIds(String responseWithChatMessages) {
    String[] parts = responseWithChatMessages
        .replaceAll(RegexPattern.CHAT_ID.regex, WRAP_1_WITH_DELIMITERS)
        .split(DELIMITER);

    List<String> list = new ArrayList<>();
    for (int i = 1; i < parts.length; i = i + 2) {
      list.add(parts[i]);
    }
    //0 {messages: [ {chat_id:
    //1 %1111111
    //2 %, message:blablalba}, {chat_id:
    //3 %2222222
    //4 %, message:bl2bla2b2}, ]

    return list;
  }

  public static List<ChatTask> parseChatTasks(String responseWithUserCommands) {
    List<ChatTask> botTasks = new ArrayList<>();
    try {
      Map map = objectMapper.readValue(responseWithUserCommands, Map.class);
      List<Map> updates = (List<Map>) map.get("result");

      for (Map update : updates) {
        if (update.containsKey(TelegramEvent.MESSAGE)) {
          Map messageMap = (Map) update.get(TelegramEvent.MESSAGE);
          String text = (String) messageMap.get("text");
          boolean startWithChatId = text.startsWith("/start ");
          if (startWithChatId) {
            Map chatMap = (Map) messageMap.get("chat");
            String sourceChatId = String.valueOf((Integer) chatMap.get("id"));

            String targetChatId = text.replaceAll("/start (.*)", "$1");
            ChatTask task = new ChatTask(ChatTask.SEND_PING, targetChatId, sourceChatId);

            botTasks.add(task);
          } else {
            Map chatMap = (Map) messageMap.get("chat");
            int chatId = (Integer) chatMap.get("id");
            String targetChatId = String.valueOf(chatId);
            ChatTask task = new ChatTask(ChatTask.SEND_QR, targetChatId, targetChatId);
            botTasks.add(task);
          }
        }
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return botTasks;
  }

  public static Optional<String> parseSentMessageId(String responseWithSentMessageId) {
    return extractFirstRegexGroup(responseWithSentMessageId, RegexPattern.MESSAGE_ID);
  }

  enum RegexPattern {
    //@formatter:off
    UPDATE_ID("\"update_id\":\\s*(\\d+),"),
    CHAT_ID("\"chat\":\\{\\s*\"id\":\\s*(\\d+),"),
    MESSAGE_ID("\"message_id\":\\s*(\\d+),"),
    ;
    //@formatter:on
    private final String regex;

    RegexPattern(String regex) {
      this.regex = regex;
    }
  }
}
