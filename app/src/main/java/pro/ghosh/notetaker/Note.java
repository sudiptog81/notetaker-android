package pro.ghosh.notetaker;

import com.google.firebase.Timestamp;

public class Note {
  private String id;
  private String title;
  private String content;
  private Timestamp timestamp;

  Note(String title, String content, Timestamp timestamp) {
    this.title = title;
    this.content = content;
    this.timestamp = timestamp;
  }

  Note(String id, String title, String content, Timestamp timestamp) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.timestamp = timestamp;
  }

  Note() {
    // empty constructor
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Timestamp getTime() {
    return timestamp;
  }

  public void setTime(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}
