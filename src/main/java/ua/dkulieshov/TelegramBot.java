package ua.dkulieshov;

import com.google.common.base.Preconditions;
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

  String buildCmdUrl(Cmd cmd) {
    return UrlUtils.buildUrl(botCommandUrlPrefix + cmd.path, Map.of());
  }

  String buildCmdUrl(Cmd cmd, Map<Param, String> parameters) {
    return UrlUtils.buildUrl(botCommandUrlPrefix + cmd.path, Map.of());
  }

  String buildStartLinkWithKeyNGroup(String chatIdOrName, String key) {
    return buildBotStartLink(Map.of(Param.STARTGROUP, chatIdOrName, Param.START, key));
  }

  private String buildBotStartLink(Map<Param, String> paramMap) {
    return selfLinkPrefix + Param.UTILS.buildTParametersSuffix(paramMap);
  }

  enum Param {
    START("start"), STARTGROUP("startgroup"), TEXT("text"), CHAT_ID("chat_id"), OFFSET("offset"),
    ;

    public static final UrlUtils<Param> UTILS = new UrlUtils<>(p -> p.key);
    private final String key;

    Param(String key) {
      this.key = key;
    }
  }

  enum Cmd {
    SEND_MESSAGE("/sendMessage"), GET_UPDATES("/getUpdates")
    /*  */;

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
