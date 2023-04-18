package com.cuala.cualabackend.config;

import com.cuala.cualabackend.chat.inbound.InboundMessageHandler;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

@Configuration
@EnableWebFlux
public class WebSocketConfig implements WebFluxConfigurer {

  private final InboundMessageHandler inboundMessageHandler;

  public WebSocketConfig(InboundMessageHandler inboundMessageHandler) {
    this.inboundMessageHandler = inboundMessageHandler;
  }

  @Bean
  public HandlerMapping reactiveWebSocketHandlerMapping() {
    Map<String, WebSocketHandler> map = new HashMap<>();
    map.put("/chat", inboundMessageHandler);
    int order = -1;
    return new SimpleUrlHandlerMapping(map, order);
  }

}
