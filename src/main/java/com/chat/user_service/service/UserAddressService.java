package com.chat.user_service.service;

import com.chat.user_service.entity.UserAddress;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserAddressService {

    Mono<UserAddress> getUserAddress(UUID userId);
    Mono<UserAddress> save(UserAddress address);
}
