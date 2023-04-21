package com.cuala.cualabackend.adapters.persistence;

import com.cuala.cualabackend.adapters.persistence.entity.MessageEntity;
import com.cuala.cualabackend.chat.domain.Message;
import com.cuala.cualabackend.chat.ports.out.MessagePort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class MessageAdapter implements MessagePort {

  private final MessageRepository messageRepository;

  public MessageAdapter(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public Mono<Message> addMessage(Message message) {
    return messageRepository.save(MessageEntity.fromDomain(message))
        .map(MessageEntity::toDomain);
  }
}
