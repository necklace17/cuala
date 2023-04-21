package com.cuala.cualabackend.adapters.persistence;

import com.cuala.cualabackend.adapters.persistence.entity.MessageEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MessageRepository extends ReactiveMongoRepository<MessageEntity, String> {

}
