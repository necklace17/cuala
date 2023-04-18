package com.cuala.cualabackend.messages;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MessageHandler implements WebSocketHandler {

  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {
    Flux<WebSocketMessage> stringFlux = webSocketSession.receive()
        .map(WebSocketMessage::getPayloadAsText)
        .map(String::toUpperCase)
        .map(webSocketSession::textMessage);
    return webSocketSession.send(stringFlux);
  }
}
