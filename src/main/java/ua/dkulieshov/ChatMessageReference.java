package ua.dkulieshov;

import com.google.common.base.Preconditions;

public class ChatMessageReference {

  public static final ChatMessageReference EMPTY = new ChatMessageReference(null, null, true);
  private final String id;
  private final String text;
  private final boolean isEmpty;

  public ChatMessageReference(String id, String text) {
    this(id, text, false);
  }

  private ChatMessageReference(String id, String text, boolean isEmpty) {
    this.id = id;
    this.text = text;
    this.isEmpty = isEmpty;
  }


  public String getId() {
    validateExists();
    return id;
  }

  private void validateExists() {
    Preconditions.checkArgument(!isEmpty);
  }

  public String getText() {
    validateExists();
    return text;
  }

  public boolean textEquals(String otherText) {
    if (isEmpty) {
      return false;
    }
    return text.equals(otherText);
  }

}
