package com.cuala.cualabackend.adapters.web.dto;

public class IncomingMessageDto {

  private String chatRoom;
  private String content;

  public IncomingMessageDto() {
  }

  public IncomingMessageDto(String chatRoom, String content) {
    this.chatRoom = chatRoom;
    this.content = content;
  }

  public String getChatRoom() {
    return chatRoom;
  }

  public String getContent() {
    return content;
  }
}
