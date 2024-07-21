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
    return friendshipService.acceptFriendRequest(acceptFriendRequest);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> denyFriendRequest(Mono<DenyFriendRequest> denyFriendRequest, ServerWebExchange exchange) {
    return friendshipService.denyFriendRequest(denyFriendRequest);
  }

  @Override
  public Mono<ResponseEntity<FriendRequestListPagingResponse>> getUserFriendRequests(Integer pageSize, Integer currentPage, ServerWebExchange exchange) {
    return friendshipService.getUserFriendRequests(pageSize, currentPage);
  }

  @Override
  public Mono<ResponseEntity<FriendsListPagingResponse>> getUserFriends(Integer pageSize, Integer currentPage, ServerWebExchange exchange) {
    return userService.getUserFriends(pageSize, currentPage);

  }

  @Override
  public Mono<ResponseEntity<UserProfileResponse>> getUserProfile(ServerWebExchange exchange) {
    return userService.getUserProfile();
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> sendFriendRequest(Mono<AddFriendRequest> addFriendRequest, ServerWebExchange exchange) {
    return friendshipService.sendFriendRequest(addFriendRequest);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfile(Mono<UpdateProfileRequest> updateProfileRequest, ServerWebExchange exchange) {
    return userService.updateUserProfile(updateProfileRequest);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfileImage(Flux<Part> avatar, ServerWebExchange exchange) {
    return UserApiDelegate.super.updateUserProfileImage(avatar, exchange);
  }
}
