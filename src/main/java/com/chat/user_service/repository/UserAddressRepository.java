package com.chat.user_service.repository;

import com.chat.user_service.entity.UserAddress;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserAddressRepository extends R2dbcRepository<UserAddress, UUID> {

  Mono<UserAddress> findByUserId(UUID userId);
}
