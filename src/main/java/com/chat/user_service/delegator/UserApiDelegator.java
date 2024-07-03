package com.chat.user_service.delegator;

import com.chat.user_service.server.api.UserApiDelegate;
import com.chat.user_service.server.model.GetUserProfile200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserApiDelegator implements UserApiDelegate {
  @Override
  public Mono<ResponseEntity<GetUserProfile200Response>> getUserProfile(ServerWebExchange exchange) {
    System.out.println("OKEEEEEEEEEEEE");
    return Mono.just(ResponseEntity.ok(new GetUserProfile200Response()));
  }
}
