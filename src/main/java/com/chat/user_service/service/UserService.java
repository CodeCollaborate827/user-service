package com.chat.user_service.service;

import com.chat.user_service.entity.User;
import com.chat.user_service.model.CommonSuccessResponse;
import com.chat.user_service.model.FriendsListPagingResponse;
import com.chat.user_service.model.UpdateProfileRequest;
import com.chat.user_service.model.UserProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<User> saveUser(User user);
    Mono<ResponseEntity<UserProfileResponse>> getUserProfile(UUID userId, String requestId);
    Mono<User> getUserById(UUID userId);

    Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfile(UUID userId, String requestId, Mono<UpdateProfileRequest> updateProfileRequest);

    Mono<ResponseEntity<FriendsListPagingResponse>> getUserFriends(UUID userId, String requestId, int pageSize, int currentPage);

    Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfileImage(UUID userId, String requestId, Flux<Part> avatar);

}
