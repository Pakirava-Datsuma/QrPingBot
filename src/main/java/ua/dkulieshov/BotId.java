package ua.dkulieshov;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bot parameters. No actual logic.
 */
public class BotId {

  public final String selfLinkPrefix;
  public final String botCommandUrlPrefix;
  private final String botName;
  private final String botToken;

  public BotId(String botName, String botToken) {
    this.botName = botName;
    this.botToken = botToken;
    selfLinkPrefix = "https://t.me/" + botName;
    botCommandUrlPrefix = "https://api.telegram.org/bot" + botToken;
  }

  String buildCmdUrl(Cmd cmd) {
    return UrlUtils.buildUrl(botCommandUrlPrefix + cmd.path, Map.of());
  }

  String buildCmdUrl(Cmd cmd, Map<Param, String> parameters) {
    return UrlUtils.buildUrl(botCommandUrlPrefix + cmd.path, Param.transformKeys(parameters));
  }

  String buildStartLinkWithKeyNGroup(String chatIdOrName, String key) {
    return buildBotStartLink(Map.of(Param.STARTGROUP, chatIdOrName, Param.START, key));
  }

  private String buildBotStartLink(Map<Param, String> paramMap) {
    String parametersSuffix = UrlUtils.buildParametersSuffix(Param.transformKeys(paramMap));
    return selfLinkPrefix + parametersSuffix;
  }

  public String mask(String s) {
    return s.replaceAll(botToken, "***");
  }

  public String buildInviteLink() {
    return buildBotStartLink(Map.of());
  }

  public String buildPingLink(String chatId) {
    String url = buildStartLinkWithKeyNGroup("", chatId);
    System.out.println("Ping link is built: " + url);
    return url;
  }

  enum Param {
    //@formatter:off
    START("start"),
    STARTGROUP("startgroup"),
    TEXT("text"),
    CHAT_ID("chat_id"),
    OFFSET("offset"),
    MESSAGE_ID("message_id"),
    PHOTO("photo"),
    ;//@formatter:on

    private final String key;

    Param(String key) {
      this.key = key;
    }

    public static Map<String, String> transformKeys(Map<Param, String> paramMap) {
      Map<String, String> map = paramMap
          .entrySet()
          .stream()
          .collect(Collectors.toMap(e -> e.getKey().key, e -> e.getValue()));
      return map;
    }

    public String getKey() {
      return key;
    }
  }

  enum Cmd {
    //@formatter:off
    SEND_MESSAGE("/sendMessage"),
    GET_UPDATES("/getUpdates"),
    DELETE_MESSAGE("/deleteMessage"),
    SEND_PHOTO("/sendPhoto"),
    GET_ME("/getMe");//@formatter:on

    private static final String SLASH_PREFIX = "/";
    private final String path;

    Cmd(String path) {
      this.path = path;
      String slash = SLASH_PREFIX;
      Preconditions.checkArgument(!path.endsWith(slash));
      Preconditions.checkArgument(path.startsWith(slash));
    }
  }
}
