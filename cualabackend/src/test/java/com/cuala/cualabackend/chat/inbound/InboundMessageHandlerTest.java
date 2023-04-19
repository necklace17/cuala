package com.cuala.cualabackend.chat.inbound;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cuala.cualabackend.chat.application.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class InboundMessageHandlerTest {

  @Mock
  private MessageService messageService;

  @Mock
  private InboundTemplateEngine inboundTemplateEngine;


  private InboundMessageHandler handler;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    handler = new InboundMessageHandler(messageService, inboundTemplateEngine);
  }

  @Test
  public void handle_shouldProcessMessageAndReturnResponse() {
    // mock input
    String messageFromClient = "hello";
    WebSocketMessage incomingMessage = mock(WebSocketMessage.class);
    when(incomingMessage.getPayloadAsText()).thenReturn(messageFromClient);
    Flux<WebSocketMessage> incomingFlux = Flux.just(incomingMessage);
    WebSocketSession webSocketSession = mock(WebSocketSession.class);
    when(webSocketSession.receive()).thenReturn(incomingFlux);

    var anotherMessage = mock(WebSocketMessage.class);
    when(anotherMessage.getPayloadAsText()).thenReturn("You did it");
    when(webSocketSession.textMessage("Response")).thenReturn(anotherMessage);

    // mock service response and template engine
    when(messageService.processMessage(messageFromClient)).thenReturn(Mono.just("world"));
    when(inboundTemplateEngine.buildSuccessfulResponse("world"))
        .thenReturn("Response");

    // Act
    handler.handle(webSocketSession);
    // Assert
    ArgumentCaptor<Flux<WebSocketMessage>> ac =
        ArgumentCaptor.forClass(Flux.class);
    verify(webSocketSession).send(ac.capture());
    var transformedClientMessage = ac.getValue()
        .map(WebSocketMessage::getPayloadAsText);
    StepVerifier.create(transformedClientMessage)
        .expectNext("You did it")
        .verifyComplete();
  }
}
