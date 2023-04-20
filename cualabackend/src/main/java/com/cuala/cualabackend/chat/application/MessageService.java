package com.cuala.cualabackend.chat.application;

import com.cuala.cualabackend.chat.domain.Message;
import com.cuala.cualabackend.chat.persistence.MessageRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageService {

  @Autowired
  private final MessageRepository messageRepository;

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public Mono<String> processMessage(String sender, String content) {
    var message = new Message(UUID.randomUUID()
        .toString(), sender, content);
    System.out.println("Try to save message..");
    messageRepository.save(message)
        .doOnError(m -> System.out.println("Error"))
        .doOnSuccess(m -> System.out.println("Message " + m + " saved"))
        .subscribe();

    return Mono.just(content);
  }


}
