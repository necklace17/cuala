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
  private final InboundTemplateEngine inboundTemplateEngine;

  public InboundMessageHandler(MessageService messageService,
      InboundTemplateEngine inboundTemplateEngine) {
    this.messageService = messageService;
    this.inboundTemplateEngine = inboundTemplateEngine;
  }

  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {
    Flux<WebSocketMessage> response = webSocketSession.receive()
        .map(WebSocketMessage::getPayloadAsText)
        .flatMap(messageService::processMessage)
        .map(inboundTemplateEngine::buildSuccessfulResponse)
        .map(webSocketSession::textMessage)
        .log();
    return webSocketSession.send(response);
  }

}
