package com.cuala.cualabackend.chat.inbound;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cuala.cualabackend.adapters.web.IncomingMessageHandler;
import com.cuala.cualabackend.adapters.web.ResponseTemplateEngine;
import com.cuala.cualabackend.chat.MessageService;
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

  private static final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
  @Mock
  private MessageService messageService;
  @Mock
  private ResponseTemplateEngine inboundTemplateEngine;
  @InjectMocks
  private IncomingMessageHandler inboundMessageHandler;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    when(messageService.addMessage(anyString(), anyString())).thenAnswer(
        invocation -> Mono.just(invocation.getArgument(1)));
    when(inboundTemplateEngine.buildSuccessfulResponse(anyString())).thenAnswer(
        invocation -> annotateSuccessMessage(invocation.getArgument(0)));
  }

  private static String annotateSuccessMessage(String inputMessage) {
    return "Success " + inputMessage;
  }

  @Test
  public void shouldHandleMessageByMessageProcessingAndTemplateCreationAndSendMessageResponse() {
    // Arrange
    String messageFromClient = "hello";
    WebSocketSession webSocketSession = createWebSocketSessionFromMessage(messageFromClient);
    // Act
    inboundMessageHandler.handle(webSocketSession);
    // Assert
    ArgumentCaptor<Flux<WebSocketMessage>> argumentCaptor = ArgumentCaptor.forClass(Flux.class);
    verify(webSocketSession).send(argumentCaptor.capture());
    StepVerifier.create(argumentCaptor.getValue()
            .map(WebSocketMessage::getPayloadAsText))
        .expectNext(annotateSuccessMessage(messageFromClient))
        .verifyComplete();
  }

  private WebSocketSession createWebSocketSessionFromMessage(String messageFromClient) {
    Flux<WebSocketMessage> incomingFlux = createIncomingFlux(messageFromClient);
    return createWebSocketSession(incomingFlux);
  }

  private static Flux<WebSocketMessage> createIncomingFlux(String messageFromClient) {
    WebSocketMessage incomingMessage = mock(WebSocketMessage.class);
    when(incomingMessage.getPayloadAsText()).thenReturn(messageFromClient);
    return Flux.just(incomingMessage);
  }

  private static WebSocketSession createWebSocketSession(Flux<WebSocketMessage> incomingFlux) {
    WebSocketSession webSocketSession = mock(WebSocketSession.class);
    when(webSocketSession.receive()).thenReturn(incomingFlux);
    when(webSocketSession.getId()).thenReturn("randomId");
    when(webSocketSession.textMessage(anyString())).thenAnswer(
        invocation -> createWebSocketMessage(invocation.getArgument(0)));
    return webSocketSession;
  }

  private static WebSocketMessage createWebSocketMessage(String message) {
    return new WebSocketMessage(Type.TEXT, dataBufferFactory.wrap(message.getBytes()));
  }


}
