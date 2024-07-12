package com.chat.user_service.delegator;


import com.chat.user_service.api.UserApiDelegate;
import com.chat.user_service.model.*;
import com.chat.user_service.service.FriendshipService;
import com.chat.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserApiDelegatorImpl implements UserApiDelegate {

  private final UserService userService;

  private final FriendshipService friendshipService;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return UserApiDelegate.super.getRequest();
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> acceptFriendRequest(Mono<AcceptFriendRequest> acceptFriendRequest, ServerWebExchange exchange) {
    String currentUser = "test";
    long requestId = 4;
    return friendshipService.acceptFriendRequest(currentUser, requestId);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> denyFriendRequest(Mono<DenyFriendRequest> denyFriendRequest, ServerWebExchange exchange) {
    String currentUser = "test";
    long requestId = 4;
    
    return friendshipService.denyFriendRequest(currentUser, requestId);
  }

  @Override
  public Mono<ResponseEntity<FriendRequestListPagingResponse>> getUserFriendRequests(ServerWebExchange exchange) {
    String userId = "test";
    int pageSize = 5;
    int currentPage = 1;
    return friendshipService.getUserFriendRequests(userId, pageSize, currentPage);
  }

  @Override
  public Mono<ResponseEntity<FriendsListPagingResponse>> getUserFriends(ServerWebExchange exchange) {
    String userId = "test";
    int pageSize = 5;
    int currentPage = 1;
    return userService.getUserFriends(userId, pageSize, currentPage);
  }

  @Override
  public Mono<ResponseEntity<UserProfileResponse>> getUserProfile(ServerWebExchange exchange) {
    String userId = "test";
    return userService.getUserProfile(userId);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> sendFriendRequest(Mono<AddFriendRequest> addFriendRequest, ServerWebExchange exchange) {
    String senderId = "test";
    String receiverId = "11";
    return friendshipService.sendFriendRequest(senderId, receiverId);
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
