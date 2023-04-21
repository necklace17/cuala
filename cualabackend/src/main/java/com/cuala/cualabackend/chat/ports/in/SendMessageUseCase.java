package com.cuala.cualabackend.chat.ports.in;

import com.cuala.cualabackend.chat.domain.Message;
import reactor.core.publisher.Mono;

public interface SendMessageUseCase {

  Mono<Message> addMessage(Message message);
}
