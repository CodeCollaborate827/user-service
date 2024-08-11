package com.chat.user_service.delegator;


import com.chat.user_service.api.UserApiDelegate;
import com.chat.user_service.model.*;
import com.chat.user_service.service.FriendshipService;
import com.chat.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserApiDelegatorImpl implements UserApiDelegate {

  private final UserService userService;

  private final FriendshipService friendshipService;

  private final String USER_ID_HEADER = "userId";
  private final String REQUEST_ID_HEADER = "requestId";

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return UserApiDelegate.super.getRequest();
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> acceptFriendRequest(Mono<AcceptFriendRequest> acceptFriendRequest, ServerWebExchange exchange) {
    String requestId = extractRequestIdFromHeader(exchange);
    UUID userId = extractUserIdFromHeader(exchange);
    return friendshipService.acceptFriendRequest(userId, requestId, acceptFriendRequest);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> denyFriendRequest(Mono<DenyFriendRequest> denyFriendRequest, ServerWebExchange exchange) {
    String requestId = extractRequestIdFromHeader(exchange);
    UUID userId = extractUserIdFromHeader(exchange);
    return friendshipService.denyFriendRequest(userId, requestId, denyFriendRequest);
  }

  @Override
  public Mono<ResponseEntity<FriendRequestListPagingResponse>> getUserFriendRequests(Integer pageSize, Integer currentPage, ServerWebExchange exchange) {
    String requestId = extractRequestIdFromHeader(exchange);
    UUID userId = extractUserIdFromHeader(exchange);
    return friendshipService.getUserFriendRequests(userId, requestId, pageSize, currentPage);
  }

  @Override
  public Mono<ResponseEntity<FriendsListPagingResponse>> getUserFriends(Integer pageSize, Integer currentPage, ServerWebExchange exchange) {
    String requestId = extractRequestIdFromHeader(exchange);
    UUID userId = extractUserIdFromHeader(exchange);
    return userService.getUserFriends(userId, requestId, pageSize, currentPage);

  }

  @Override
  public Mono<ResponseEntity<UserProfileResponse>> getUserProfile(ServerWebExchange exchange) {
    String requestId = extractRequestIdFromHeader(exchange);
    UUID userId = extractUserIdFromHeader(exchange);
    return userService.getUserProfile(userId, requestId);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> sendFriendRequest(Mono<AddFriendRequest> addFriendRequest, ServerWebExchange exchange) {
    String requestId = extractRequestIdFromHeader(exchange);
    UUID userId = extractUserIdFromHeader(exchange);
    return friendshipService.sendFriendRequest(userId, requestId, addFriendRequest);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfile(Mono<UpdateProfileRequest> updateProfileRequest, ServerWebExchange exchange) {
    String requestId = extractRequestIdFromHeader(exchange);
    UUID userId = extractUserIdFromHeader(exchange);
    return userService.updateUserProfile(userId, requestId, updateProfileRequest);
  }

  @Override
  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfileImage(Flux<Part> avatar, ServerWebExchange exchange) {
    String requestId = extractRequestIdFromHeader(exchange);
    UUID userId = extractUserIdFromHeader(exchange);
    return userService.updateUserProfileImage(userId, requestId, avatar);
  }
  private String extractRequestIdFromHeader(ServerWebExchange exchange) {
    String requestId = null;
    if (exchange.getRequest().getHeaders().containsKey(USER_ID_HEADER)) {
      requestId = exchange.getRequest().getHeaders().get(USER_ID_HEADER).get(0);
    }

    if (requestId == null) {
      return UUID.randomUUID().toString();
    }
    return requestId;
  }

  private UUID extractUserIdFromHeader(ServerWebExchange exchange) {
    String userId = null;
    if (exchange.getRequest().getHeaders().containsKey(REQUEST_ID_HEADER)) {
      userId = exchange.getRequest().getHeaders().get(REQUEST_ID_HEADER).get(0);
    }

    if (userId == null) {
      throw new RuntimeException("User ID not found in header");
    }
    return UUID.fromString(userId);
  }
}
