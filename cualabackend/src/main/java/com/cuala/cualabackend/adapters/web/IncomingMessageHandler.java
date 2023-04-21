package com.cuala.cualabackend.adapters.web;

import com.cuala.cualabackend.adapters.web.dto.IncomingMessageDto;
import com.cuala.cualabackend.chat.MessageService;
import com.cuala.cualabackend.chat.domain.Message;
import com.cuala.cualabackend.chat.ports.in.SendMessageUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class IncomingMessageHandler implements WebSocketHandler {

  private final SendMessageUseCase sendMessageUseCase;
  private final ResponseTemplateEngine inboundTemplateEngine;
//  private ObjectMapper objectMapper;

  public IncomingMessageHandler(MessageService messageService,
      ResponseTemplateEngine inboundTemplateEngine) {
    this.sendMessageUseCase = messageService;
    this.inboundTemplateEngine = inboundTemplateEngine;
  }

  // TODO: input typing
  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {
    var response = webSocketSession.receive()
        .map(WebSocketMessage::getPayloadAsText)
        .map(this::parseMessageDto)
        .map(incomingMessageDto -> createMessage(webSocketSession.getId(), incomingMessageDto))
        .flatMap(sendMessageUseCase::addMessage)
        .map(Message::getContent)
        .map(inboundTemplateEngine::buildSuccessfulResponse)
        .map(webSocketSession::textMessage);
    return webSocketSession.send(response);
  }

  private IncomingMessageDto parseMessageDto(String s) {
    var objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(s, IncomingMessageDto.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to parse message", e);
    }
  }

  private Message createMessage(String sender, IncomingMessageDto incomingMessageDto) {
    return new Message(sender, incomingMessageDto.getChatRoom(), incomingMessageDto.getChatRoom());

  }

}
