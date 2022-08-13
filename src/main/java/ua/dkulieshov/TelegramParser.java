package ua.dkulieshov;

import java.util.Optional;

public class TelegramParser {


  public static final String DELIMITER = "%";
  public static final String WRAP_1_WITH_DELIMITERS = DELIMITER + "$1" + DELIMITER;

  public Optional<String> parseUpdateOffset(String response) {
    String[] parts = response.replaceAll("", WRAP_1_WITH_DELIMITERS).split(DELIMITER);
    if (parts.length < 2) {
      return Optional.empty();
    }
    return Optional.ofNullable(parts[2]);
  }
}
