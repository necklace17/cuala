package com.cuala.cualabackend.chat.persistence;

import com.cuala.cualabackend.chat.domain.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public
interface MessageRepository extends ReactiveMongoRepository<Message, String> {

}
