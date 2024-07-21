package com.chat.user_service.repository;

import com.chat.user_service.entity.FriendRequest;
import com.chat.user_service.model.FriendRequestDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface FriendRequestRepository extends R2dbcRepository<FriendRequest, UUID> {

  @Query("""
          SELECT 
            fr.id as request_id, 
            fr.created_at as sent_at,
            fr.status as status,
            u_sender.id AS sender_id,
            u_sender.username as sender_username,
            u_sender.display_name as sender_display_name, 
            u_sender.avatar_url as sender_avatar_url
          FROM friend_requests fr
          JOIN users u_sender ON fr.sender_id = u_sender.id
          WHERE fr.recipient_id = :userId AND fr.status = :status
          ORDER BY fr.created_at DESC 
          OFFSET :offset LIMIT :limit
          """)
  Flux<FriendRequestDTO> findFriendRequestOfUserPaging(UUID userId, int offset, int limit, FriendRequest.Status status);

  Mono<Integer> countByRecipientIdAndStatus(UUID userId, FriendRequest.Status status);



  Mono<FriendRequest> findBySenderIdAndRecipientIdAndStatus(UUID senderId, UUID recipientId, FriendRequest.Status status);
}
