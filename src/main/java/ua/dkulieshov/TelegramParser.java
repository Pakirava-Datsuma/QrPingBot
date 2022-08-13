package ua.dkulieshov;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class TelegramParser {


  public static final String DELIMITER = "%";
  public static final String WRAP_1_WITH_DELIMITERS = DELIMITER + "$1" + DELIMITER;

  public static String parseUpdateOffset(String response) {
    return extractOne(response, RegexPattern.UPDATE_RESPONSE_WITH_1_OFFSET_GROUP_REGEX);
  }

  private static String extractOne(String response, RegexPattern regex) {
    String[] parts = response.replaceAll(regex.regex, WRAP_1_WITH_DELIMITERS).split(DELIMITER);

    return parts[1];
  }

  public static Stream<String> parseChatIds(String responseWithChatMessages) {
    if (responseWithChatMessages.isBlank()) {
      return Stream.empty();
    }

    String[] parts = responseWithChatMessages.replaceAll(
            RegexPattern.MESSAGES_RESPONSE_WITH_1_CHAT_ID_GROUP_REGEX.regex, WRAP_1_WITH_DELIMITERS)
        .split(DELIMITER);

    Builder<String> builder = Stream.<String>builder();
    for (int i = 1; i < parts.length; i = i + 2) {
      builder.accept(parts[i]);
    }

    return builder.build();
  }

  public static String parseSentMessageId(String responseWithSentMessageId) {
    return extractOne(responseWithSentMessageId,
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
