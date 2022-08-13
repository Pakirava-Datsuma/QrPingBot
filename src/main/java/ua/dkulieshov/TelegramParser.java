package ua.dkulieshov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TelegramParser {


  public static final String DELIMITER = "%";
  public static final String WRAP_1_WITH_DELIMITERS = DELIMITER + "$1" + DELIMITER;

  public static Optional<String> parseUpdateOffset(String response) {
    return extractFirstRegexGroup(response, RegexPattern.UPDATE_RESPONSE_WITH_1_OFFSET_GROUP_REGEX);
  }

  private static Optional<String> extractFirstRegexGroup(String response, RegexPattern regex) {
    String[] parts = response.replaceAll(regex.regex, WRAP_1_WITH_DELIMITERS).split(DELIMITER);
    Optional<String> maybeSecondPart = Optional.of(parts).filter(array -> array.length > 1)
        .map(array -> array[1]);

    if (maybeSecondPart.isEmpty()) {
      System.out.println("Not found first group. Parts: " + Arrays.toString(parts));
    }

    return maybeSecondPart;
  }

  public static List<String> parseChatIds(String responseWithChatMessages) {
    String[] parts = responseWithChatMessages.replaceAll(
            RegexPattern.MESSAGES_RESPONSE_WITH_1_CHAT_ID_GROUP_REGEX.regex, WRAP_1_WITH_DELIMITERS)
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

  public static Optional<String> parseSentMessageId(String responseWithSentMessageId) {
    return extractFirstRegexGroup(responseWithSentMessageId,
        RegexPattern.SENT_MESSAGE_RESPONSE_WITH_1ST_MESSAGE_ID_GROUP_REGEX);
  }

  enum RegexPattern {
    UPDATE_RESPONSE_WITH_1_OFFSET_GROUP_REGEX(
        "\"update\":\\s+(\\d+),"), MESSAGES_RESPONSE_WITH_1_CHAT_ID_GROUP_REGEX(
        "\"chat_id\":\\s+(\\d+),"), SENT_MESSAGE_RESPONSE_WITH_1ST_MESSAGE_ID_GROUP_REGEX(
        "\"message_id\":\\s+(\\d+),"),
    /*  */;
    private final String regex;

    RegexPattern(String regex) {
      this.regex = regex;
    }

  }
}
