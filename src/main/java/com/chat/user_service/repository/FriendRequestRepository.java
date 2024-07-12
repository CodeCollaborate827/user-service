package com.chat.user_service.repository;

import com.chat.user_service.entity.FriendRequest;
import com.chat.user_service.model.FriendRequestDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FriendRequestRepository extends R2dbcRepository<FriendRequest, Long> {

  @Query("""
          SELECT 
            fr.id as request_id, 
            fr.created_at as sent_at,
            u_sender.id AS sender_id,
            u_sender.username as sender_username,
            u_sender.display_name as sender_display_name, 
            u_sender.avatar_url as sender_avatar_url
          FROM friend_requests fr
          JOIN users u_sender ON fr.sender_id = u_sender.id
          WHERE fr.recipient_id = :userId
          OFFSET :offset LIMIT :limit
          """)
  Flux<FriendRequestDTO> findFriendRequestOfUserPaging(String userId, int offset, int limit);

  Mono<Integer> countByRecipientId(String userId);
}
