package ua.dkulieshov;

public class ChatTask {

  public static final String SEND_PING = "SEND_PING";
  final String action;
  final String chatId;

  public ChatTask(String action, String chatId) {
    this.action = action;
    // checking whether the chatId is a number
    this.chatId = String.valueOf(Long.parseLong(chatId));
  }

  @Override
  public String toString() {
    return "ChatTask{" + "action='" + action + '\'' + ", chatId='" + chatId + '\'' + '}';
  }
}
