package com.chat.user_service.service;

import com.chat.user_service.entity.UserAddress;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UserAddressService {

  Mono<UserAddress> getUserAddress(UUID userId);

  Mono<UserAddress> save(UserAddress address);
}
