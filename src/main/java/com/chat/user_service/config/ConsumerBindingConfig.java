package com.chat.user_service.config;


import com.chat.user_service.entity.User;
import com.chat.user_service.event.Event;
import com.chat.user_service.event.UserRegistrationEvent;
import com.chat.user_service.service.UserService;
import com.chat.user_service.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Base64;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ConsumerBindingConfig {


  private final ObjectMapper objectMapper;
  private final UserService userService;

  @Bean
  public Consumer<Flux<Message<Event>>> userRegistrationDownstreamConsumer() {
    return flux -> flux.flatMap(message -> processMessage(message.getPayload()))
            .onErrorResume(this::handleError)
            .subscribe();

  }

  private Mono<User> handleError(Throwable e) {
    log.error("Error when consuming userRegistrationEvent: {}", e.getMessage());

    // return Mono containing empty user, so the flux can continue with other item
    return Mono.just(new User());
  }

  private Mono<User> processMessage(Event event) {
    log.info("Processing the message");
    String payloadBase64 = event.getPayloadBase64();
    UserRegistrationEvent userRegistrationEvent = null;
    try {
      log.info("Parsing the event");
      userRegistrationEvent = parseUserRegistrationEvent(payloadBase64);
      User user = Utils.convertToUser(userRegistrationEvent);
      log.info("convertToUser done: {}", user);
      return userService.saveUser(user);
    } catch (JsonProcessingException e) {
      return Mono.error(e);
    }


  }

  public UserRegistrationEvent parseUserRegistrationEvent(String encodedBase64) throws JsonProcessingException {
    byte[] decoded = Base64.getDecoder().decode(encodedBase64);
    String json = new String(decoded);
    return objectMapper.readValue(json, UserRegistrationEvent.class);
  }
}
