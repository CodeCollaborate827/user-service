package com.chat.user_service.delegator;

import com.chat.user_service.server.api.UserApiDelegate;
import com.chat.user_service.server.model.*;
import com.chat.user_service.service.FriendshipService;
import com.chat.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class UserApiDelegator implements UserApiDelegate {

  @Autowired
  private UserService userService;

  @Autowired
  private FriendshipService friendshipService;

  @Override
  public Mono<ResponseEntity<GetUserProfile200Response>> getUserProfile(ServerWebExchange exchange) {
    String testId = "test";
    return userService.getUserProfile(testId);
  }

  @Override
  public Mono<ResponseEntity<AcceptFriendRequest200Response>> acceptFriendRequest(Mono<FriendRequestId> friendRequestId, ServerWebExchange exchange) {
    return UserApiDelegate.super.acceptFriendRequest(friendRequestId, exchange);
  }

  @Override
  public Mono<ResponseEntity<DenyFriendRequest200Response>> denyFriendRequest(Mono<FriendRequestId> friendRequestId, ServerWebExchange exchange) {
    return UserApiDelegate.super.denyFriendRequest(friendRequestId, exchange);
  }

  @Override
  public Mono<ResponseEntity<GetUserFriendRequests200Response>> getUserFriendRequests(ServerWebExchange exchange) {
    return UserApiDelegate.super.getUserFriendRequests(exchange);
  }

  @Override
  public Mono<ResponseEntity<GetUserFriends200Response>> getUserFriends(ServerWebExchange exchange) {
    String testId = "test";
    return friendshipService.getUserFriends(testId);
  }

  @Override
  public Mono<ResponseEntity<SendFriendRequest200Response>> sendFriendRequest(Mono<AddFriendRequest> addFriendRequest, ServerWebExchange exchange) {
    return UserApiDelegate.super.sendFriendRequest(addFriendRequest, exchange);
  }

  @Override
  public Mono<ResponseEntity<UpdateUserProfile200Response>> updateUserProfile(Mono<UpdateProfileRequest> updateProfileRequest, ServerWebExchange exchange) {
    return userService.updateUserProfile(updateProfileRequest);
  }

  @Override
  public Mono<ResponseEntity<UpdateUserProfileImage200Response>> updateUserProfileImage(Flux<Part> avatar, ServerWebExchange exchange) {
    return UserApiDelegate.super.updateUserProfileImage(avatar, exchange);
  }
}
