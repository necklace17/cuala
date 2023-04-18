package com.cuala.cualabackend.chat.inbound;

import com.cuala.cualabackend.chat.application.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class InboundMessageHandler implements WebSocketHandler {

  private final MessageService messageService;

  public InboundMessageHandler(MessageService messageService) {
    this.messageService = messageService;
  }

  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {
    Flux<WebSocketMessage> output = webSocketSession.receive()
        .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
        .flatMap(messageService::processMessage)
        .map(value -> webSocketSession.textMessage("Echo " + value));
    return webSocketSession.send(output);
  }
}
