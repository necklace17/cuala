package com.cuala.cualabackend.adapters.web;

import org.springframework.stereotype.Component;

@Component
public class ResponseTemplateEngine {

  String buildSuccessfulResponse(String value) {
    return "Message: \"%s\" sent successful".formatted(value);
  }
}
