package com.chat.user_service.repository;

import com.chat.user_service.entity.FriendRequest;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends R2dbcRepository<FriendRequest, Long> {
}
