package com.cuala.cualabackend.chat.inbound;

import org.springframework.stereotype.Component;

@Component
public class InboundTemplateEngine {

  String buildSuccessfulResponse(String value) {
    return "Message: \"%s\" sent successful".formatted(value);
  }
}
