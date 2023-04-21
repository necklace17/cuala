package com.cuala.cualabackend.chat.ports.out;

import com.cuala.cualabackend.chat.domain.Message;
import reactor.core.publisher.Mono;

public interface MessagePort {

  Mono<Message> addMessage(Message message);

}
