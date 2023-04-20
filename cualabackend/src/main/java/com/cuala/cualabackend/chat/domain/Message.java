package com.cuala.cualabackend.chat.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Data
@ToString
public class Message {

  @Id
  private String id;
  private String sender;
  private String content;

  public Message(String id, String sender, String content) {
    this.id = id;
    this.sender = sender;
    this.content = content;
  }
}
