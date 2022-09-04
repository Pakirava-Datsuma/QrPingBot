package ua.dkulieshov;

public class ChatTask {

  public static final String SEND_PING = "SEND_PING";
  public static final String SEND_QR = "SEND_QR";
  final String action;
  final String targetChatId;
  final String sourceChatId;

  public ChatTask(String action, String targetChatId, String sourceChatId) {
    this.action = action;
    this.targetChatId = validateChatId(targetChatId);
    this.sourceChatId = validateChatId(sourceChatId);
  }

  private static String validateChatId(String targetChatId) {
    // checking whether the chatId is a number
    return String.valueOf(Long.parseLong(targetChatId));
  }

  @Override
  public String toString() {
    return "ChatTask{" + "action='" + action + '\'' + ", chatId='" + targetChatId + '\'' + '}';
  }
}
