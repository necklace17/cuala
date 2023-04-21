package com.cuala.cualabackend.adapters.web;

import com.cuala.cualabackend.chat.MessageService;
import com.cuala.cualabackend.chat.domain.Message;
import com.cuala.cualabackend.chat.ports.in.SendMessageUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class IncomingMessageHandler implements WebSocketHandler {

  private final SendMessageUseCase sendMessageUseCase;
  private final ResponseTemplateEngine inboundTemplateEngine;

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
        .flatMap(
            messageContent -> sendMessageUseCase.addMessage(
                    toMessage(webSocketSession.getId(), messageContent))
                .map(this::messageToString)
                .map(inboundTemplateEngine::buildSuccessfulResponse)
                .map(webSocketSession::textMessage));
    return webSocketSession.send(response);
  }

  private Message toMessage(String sender, String messageContent) {
    return new Message(sender, messageContent);
  }

  private String messageToString(Message message) {
    return message.getContent();
  }

}
