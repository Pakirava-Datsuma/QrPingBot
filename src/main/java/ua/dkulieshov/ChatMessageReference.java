package ua.dkulieshov;

import java.util.Optional;

public class ChatMessageReference {

  private Optional<String> id = Optional.empty();
  private Optional<String> text = Optional.empty();

  public void update(String id, String text) {
    this.id = Optional.of(id);
    this.text = Optional.of(text);
  }

  public String getId() {
    return id.get();
  }

  public String getText() {
    return text.get();
  }

  public boolean textEquals(String otherText) {
    return text.isPresent() && text.get().equals(otherText);
    return text.filter(otherText::equals).isPresent();
  }
}
