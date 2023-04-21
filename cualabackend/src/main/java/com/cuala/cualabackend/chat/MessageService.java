package com.cuala.cualabackend.chat;

import com.cuala.cualabackend.chat.domain.Message;
import com.cuala.cualabackend.chat.ports.in.SendMessageUseCase;
import com.cuala.cualabackend.chat.ports.out.MessagePort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageService implements SendMessageUseCase {


  private final MessagePort messagePort;

  public MessageService(MessagePort messagePort) {
    this.messagePort = messagePort;
  }

  @Override
  public Mono<Message> addMessage(Message message) {
    return messagePort.addMessage(message);

  }
}
