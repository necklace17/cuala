package com.cuala.cualabackend.chat.application;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageService {

  public Mono<String> processMessage(String message) {
    System.out.println("Processed message: " + message);
    return Mono.just(message);
  }
}
