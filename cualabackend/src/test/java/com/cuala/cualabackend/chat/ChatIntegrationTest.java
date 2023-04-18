package com.cuala.cualabackend.chat.inbound;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatIntegrationTest {

  private static final String INCOMING_CHAT_PATH = "/chat";
  private static WebSocketClient client;
  @LocalServerPort
  private String port;

  @BeforeAll
  static void setup() {
    client = new ReactorNettyWebSocketClient();
  }

  @Test
  public void newTest() {
    // Arrange
    var message = "Hello there";
    var expectedReturnMessage = "Message: \"%s\" sent successful".formatted(message);
    // Act
    var result = client.execute(
        createChatUri(),
        session -> session.send(
                Mono.just(session.textMessage(message)))
            .thenMany(session.receive()
                .map(WebSocketMessage::getPayloadAsText)
            )
            .take(1)
            .doOnNext(returnMessage -> assertThat(returnMessage).isEqualTo(expectedReturnMessage))
            .then());
    // Assert
    StepVerifier.create(result)
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(5)) // Wait for up to 5 seconds for the test to complete
        .expectComplete()
        .verify();
  }

  private URI createChatUri() {
    return UriComponentsBuilder.newInstance()
        .scheme("ws")
        .host("localhost")
        .port(port)
        .path(INCOMING_CHAT_PATH)
        .build()
        .toUri();
  }
}
