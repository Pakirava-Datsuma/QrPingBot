package ua.dkulieshov;

import com.google.common.base.Preconditions;

public class ChatMessageReference {

  public static final ChatMessageReference EMPTY = new ChatMessageReference(null, null, true, 0);
  private final String id;
  private final String text;
  private final boolean isEmpty;

  private int repetitionNumber = 0;

  public ChatMessageReference(String id, String text) {
    this(id, text, false, 1);
  }

  private ChatMessageReference(String id, String text, boolean isEmpty, int repetitionNumber) {
    this.id = id;
    this.text = text;
    this.isEmpty = isEmpty;
    this.repetitionNumber = repetitionNumber;
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

  public int incrementAndGetRepetition() {
    return ++repetitionNumber;
  }

}
