package com.chat.user_service.service;

import com.chat.user_service.model.*;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FriendshipService {
    Mono<ResponseEntity<FriendRequestListPagingResponse>> getUserFriendRequests(UUID userId, String requestId, int pageSize, int currentPage);
    Mono<ResponseEntity<CommonSuccessResponse>> sendFriendRequest(UUID senderId, String requestId, Mono<AddFriendRequest> addFriendRequest);
    Mono<ResponseEntity<CommonSuccessResponse>> acceptFriendRequest(UUID recipientId, String requestId, Mono<AcceptFriendRequest> acceptFriendRequest);
    Mono<ResponseEntity<CommonSuccessResponse>> denyFriendRequest(UUID currentUserId, String requestId, Mono<DenyFriendRequest> DenyFriendRequest);
}
