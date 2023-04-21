package com.cuala.cualabackend.chat.domain;

public class Message {

  private final String sender;
  private final String content;
  private String id;

  public Message(String sender, String content) {
    this.sender = sender;
    this.content = content;
  }

  public Message(String id, String sender, String content) {
    this.id = id;
    this.sender = sender;
    this.content = content;
  }

  public String getSender() {
    return sender;
  }

  public String getContent() {
    return content;
  }

}
