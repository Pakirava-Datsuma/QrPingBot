package ua.dkulieshov;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class TelegramParser {


  public static final String DELIMITER = "%";
  public static final String WRAP_1_WITH_DELIMITERS = DELIMITER + "$1" + DELIMITER;
  private static final String UPDATE_RESPONSE_WITH_1_OFFSET_GROUP_REGEX = "\"update\":\\s+(\\d+),";
  private static final String MESSAGES_RESPONSE_WITH_1_CHAT_ID_GROUP_REGEX = "\"chat_id\":\\s+(\\d+),";

  public static String parseUpdateOffset(String response) {
    String[] parts = response.replaceAll(UPDATE_RESPONSE_WITH_1_OFFSET_GROUP_REGEX,
        WRAP_1_WITH_DELIMITERS).split(DELIMITER);
    if (parts.length < 2) {
      //      return null;
    }
    //    return Optional.ofNullable(parts[1]);
    return parts[1];
  }

  public static Stream<String> parseChatIds(String responseWithChatMessages) {
    if (responseWithChatMessages.isBlank()) {
      return Stream.empty();
    }

    String[] parts = responseWithChatMessages.replaceAll(
        MESSAGES_RESPONSE_WITH_1_CHAT_ID_GROUP_REGEX, WRAP_1_WITH_DELIMITERS).split(DELIMITER);

    Builder<String> builder = Stream.<String>builder();
    for (int i = 1; i < parts.length; i = i + 2) {
      builder.accept(parts[i]);
    }

    return builder.build();
  }
}
