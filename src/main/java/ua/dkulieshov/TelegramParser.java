package ua.dkulieshov;

public class TelegramParser {


  public static final String DELIMITER = "%";
  public static final String WRAP_1_WITH_DELIMITERS = DELIMITER + "$1" + DELIMITER;

  public static String parseUpdateOffset(String response) {
    String[] parts = response.replaceAll("", WRAP_1_WITH_DELIMITERS).split(DELIMITER);
    if (parts.length < 2) {
      //      return null;
    }
    //    return Optional.ofNullable(parts[1]);
    return parts[1];
  }
}
