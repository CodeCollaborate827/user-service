package com.chat.user_service.service.impl;

import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.repository.UserAddressRepository;
import com.chat.user_service.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

  private final UserAddressRepository userAddressRepository;


  public Mono<UserAddress> getUserAddress(UUID userId) {
    return userAddressRepository.findByUserId(userId);
  }

  public Mono<UserAddress> save(UserAddress address) {
    return userAddressRepository.save(address);
  }
}
