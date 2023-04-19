package com.cuala.cualabackend.chat.inbound;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cuala.cualabackend.chat.application.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketMessage.Type;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class InboundMessageHandlerTest {

  @Mock
  private MessageService messageService;

  @Mock
  private InboundTemplateEngine inboundTemplateEngine;
  @InjectMocks
  private InboundMessageHandler inboundMessageHandler;


  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

  }


  @Test
  public void handle_shouldProcessMessageAndReturnResponse() {
    // Arrange
    String messageFromClient = "hello";
    WebSocketSession webSocketSession = createWebSocketSessionFromMessage(messageFromClient);
    var successMessage = "Success " + messageFromClient;
    mockDependencies(messageFromClient, successMessage);
    // Act
    inboundMessageHandler.handle(webSocketSession);
    // Assert
    ArgumentCaptor<Flux<WebSocketMessage>> argumentCaptor = ArgumentCaptor.forClass(Flux.class);
    verify(webSocketSession).send(argumentCaptor.capture());
    StepVerifier.create(argumentCaptor.getValue()
            .map(WebSocketMessage::getPayloadAsText))
        .expectNext(successMessage)
        .verifyComplete();
  }

  private WebSocketSession createWebSocketSessionFromMessage(String messageFromClient) {
    Flux<WebSocketMessage> incomingFlux = createIncomingFlux(messageFromClient);
    return createWebSocketSession(incomingFlux);
  }

  private void mockDependencies(String messageFromClient, String successMessage) {
    when(messageService.processMessage(messageFromClient)).thenReturn(
        Mono.just(messageFromClient));
    when(inboundTemplateEngine.buildSuccessfulResponse(messageFromClient)).thenReturn(
        successMessage);
  }

  private static Flux<WebSocketMessage> createIncomingFlux(String messageFromClient) {
    WebSocketMessage incomingMessage = mock(WebSocketMessage.class);
    when(incomingMessage.getPayloadAsText()).thenReturn(messageFromClient);
    return Flux.just(incomingMessage);
  }

  private static WebSocketSession createWebSocketSession(Flux<WebSocketMessage> incomingFlux) {
    WebSocketSession webSocketSession = mock(WebSocketSession.class);
    when(webSocketSession.receive()).thenReturn(incomingFlux);
    DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
    when(webSocketSession.textMessage(anyString()))
        .thenAnswer(invocation -> {
          String arg = invocation.getArgument(0);
          return new WebSocketMessage(Type.TEXT,
              dataBufferFactory.wrap(arg.getBytes()));
        });
    return webSocketSession;
  }
}
