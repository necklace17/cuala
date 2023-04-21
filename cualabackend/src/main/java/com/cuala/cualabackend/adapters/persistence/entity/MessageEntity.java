package com.cuala.cualabackend.adapters.persistence.entity;

import com.cuala.cualabackend.chat.domain.Message;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
public
class MessageEntity {

  @Id
  private String id;
  private String sender;
  private String chatRoom;
  private String content;

  public MessageEntity(String sender, String chatRoom, String content) {
    this.sender = sender;
    this.chatRoom = chatRoom;
    this.content = content;
  }

  public static MessageEntity fromDomain(Message message) {
    return new MessageEntity(
        message.getSender(),
        message.getContent()
    );
  }

  public Message toDomain() {
    return new Message(id, sender, content);
  }

}
