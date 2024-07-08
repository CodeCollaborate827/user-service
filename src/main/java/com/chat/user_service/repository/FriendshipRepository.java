package com.chat.user_service.repository;

import com.chat.user_service.entity.FriendShip;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FriendshipRepository extends R2dbcRepository<FriendShip, Long> {

  Flux<FriendShip> findAllByUser1IdOrOrUser2Id(String userId);
}
