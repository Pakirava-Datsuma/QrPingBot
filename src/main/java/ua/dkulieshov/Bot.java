package ua.dkulieshov;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bot parameters. No actual logic.
 */
public class Bot {

  public final String selfLinkPrefix;
  public final String botCommandUrlPrefix;
  private final String botName;
  private final String botToken;

  public Bot(String botName, String botToken) {
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

  public void log() {
    System.out.println();
  }

  public void log(String s) {
    String maskedS = s.replaceAll(botToken, "***");
    System.out.println(maskedS);
  }

  enum Param {
    //@formatter:off
    START("start"),
    STARTGROUP("startgroup"),
    TEXT("text"),
    CHAT_ID("chat_id"),
    OFFSET("offset"),
    MESSAGE_ID("message_id"),
    ;//@formatter:on


    private final String key;

    Param(String key) {
      this.key = key;
    }

    public static Map<String, String> transformKeys(Map<Param, String> paramMap) {
      Map<String, String> map = paramMap.entrySet().stream()
          .collect(Collectors.toMap(e -> e.getKey().key, e -> e.getValue()));
      return map;
    }
  }

  enum Cmd {
    //@formatter:off
    SEND_MESSAGE("/sendMessage"),
    GET_UPDATES("/getUpdates"),
    DELETE_MESSAGE("/deleteMessage")
    ;//@formatter:on

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
