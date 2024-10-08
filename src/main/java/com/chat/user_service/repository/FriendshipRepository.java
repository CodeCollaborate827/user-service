package com.chat.user_service.repository;

import com.chat.user_service.entity.FriendShip;
import com.chat.user_service.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface FriendshipRepository extends R2dbcRepository<FriendShip, UUID> {

  Mono<Integer> countByUser1IdOrUser2Id(UUID userId1, UUID userId2);


  @Query("""
          SELECT *
          FROM users u
          WHERE u.id IN (
              SELECT
                  CASE
                      WHEN f.user1_id = :userId THEN f.user2_id
                      ELSE f.user1_id
                  END AS id
              FROM friendships f
              WHERE f.user1_id = :userId OR f.user2_id = :userId
              ORDER BY f.created_at
          )
          OFFSET :offset LIMIT :limit;
          """
  )
  Flux<User> findUserFriendsPaging(UUID userId, int offset, int limit);

  @Query("""
          SELECT * 
          FROM friendships f 
          WHERE 
          (f.user1_id = :userId1 AND f.user2_id = :userId2) 
          OR 
          (f.user2_id = :userId1 AND f.user1_id = :userId2)
          """)
  Mono<FriendShip> findFriendShipBetween2Users(UUID userId1, UUID userId2);
}
