package ua.dkulieshov;

import java.util.Map;

public class TelegramBot {

  public final String selfLinkPrefix;
  public final String botCommandUrlPrefix;
  private final String botName;
  private final String botToken;

  public TelegramBot(String botName, String botToken) {

    this.botName = botName;
    this.botToken = botToken;
    selfLinkPrefix = "https://t.me/" + botName;
    botCommandUrlPrefix = "https://api.telegram.org/bot" + botToken;
  }

  String buildBotStartForKey(String key) {
    return buildBotStartLink(Map.of(ParamName.START, key));
  }

  private String buildBotStartLink(Map<String, String> paramMap) {
    return selfLinkPrefix + UrlUtils.buildParametersSuffix(paramMap);
  }

  String buildBotStartForGroup(String chatIdOrName) {
    return buildBotStartLink(Map.of(ParamName.STARTGROUP, chatIdOrName));
  }

  String buildBotStartForKeyNGroup(String chatIdOrName, String key) {
    return buildBotStartLink(Map.of(ParamName.STARTGROUP, chatIdOrName, ParamName.START, key));
  }

  static class ParamName {

    public static final String START = "start";
    public static final String STARTGROUP = "startgroup";
    public static final String TEXT = "text";
    public static final String CHAT_ID = "chat_id";
    static final String OFFSET = "offset";
  }

  public class CmdPath {

    static final String SEND_MESSAGE = "/sendMessage";
    static final String GET_UPDATES = "/getUpdates";
  }
}
