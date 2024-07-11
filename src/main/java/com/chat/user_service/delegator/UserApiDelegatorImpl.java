package com.chat.user_service.delegator;


import com.chat.user_service.api.UserApiDelegate;
import com.chat.user_service.model.*;
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
public class UserApiDelegatorImpl implements UserApiDelegate {

  @Autowired
  private UserService userService;

  @Autowired
  private FriendshipService friendshipService;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return UserApiDelegate.super.getRequest();
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> acceptFriendRequest(Mono<AcceptFriendRequest> acceptFriendRequest, ServerWebExchange exchange) {
    return UserApiDelegate.super.acceptFriendRequest(acceptFriendRequest, exchange);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> denyFriendRequest(Mono<DenyFriendRequest> denyFriendRequest, ServerWebExchange exchange) {
    return UserApiDelegate.super.denyFriendRequest(denyFriendRequest, exchange);
  }

  @Override
  public Mono<ResponseEntity<FriendRequestListPagingResponse>> getUserFriendRequests(ServerWebExchange exchange) {
    return UserApiDelegate.super.getUserFriendRequests(exchange);
  }

  @Override
  public Mono<ResponseEntity<FriendsListPagingResponse>> getUserFriends(ServerWebExchange exchange) {
    return UserApiDelegate.super.getUserFriends(exchange);
  }

  @Override
  public Mono<ResponseEntity<UserProfileResponse>> getUserProfile(ServerWebExchange exchange) {
    String userId = "test";
    return userService.getUserProfile(userId);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> sendFriendRequest(Mono<AddFriendRequest> addFriendRequest, ServerWebExchange exchange) {
    return UserApiDelegate.super.sendFriendRequest(addFriendRequest, exchange);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfile(Mono<UpdateProfileRequest> updateProfileRequest, ServerWebExchange exchange) {
    String userId = "test";
    return userService.updateUserProfile(updateProfileRequest, userId);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfileImage(Flux<Part> avatar, ServerWebExchange exchange) {
    return UserApiDelegate.super.updateUserProfileImage(avatar, exchange);
  }
}
