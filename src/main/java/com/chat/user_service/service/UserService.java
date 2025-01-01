package com.chat.user_service.service;

import com.chat.user_service.entity.User;
import com.chat.user_service.model.*;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
  Mono<User> saveUser(User user);

  Mono<ResponseEntity<UserProfileResponse>> getUserProfile(UUID userId, String requestId);

  Mono<User> getUserById(UUID userId);

  Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfile(
      UUID userId, String requestId, Mono<UpdateProfileRequest> updateProfileRequest);

  Mono<ResponseEntity<FriendsListPagingResponse>> getUserFriends(
      UUID userId, String requestId, int pageSize, int currentPage);

  Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfileImage(
      UUID userId, String requestId, Flux<Part> avatar);

  Mono<ResponseEntity<UserSearchPagingResponse>> searchUsers(
      String requestId, String keyword, Integer pageSize, Integer currentPage);
}
