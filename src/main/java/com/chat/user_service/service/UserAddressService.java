package com.chat.user_service.service;

import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.repository.UserAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserAddressService {

  @Autowired
  private UserAddressRepository userAddressRepository;


  public Mono<UserAddress> getUserAddress(UUID userId) {
    return userAddressRepository.findByUserId(userId);
  }

  public Mono<UserAddress> save(UserAddress address) {
    return userAddressRepository.save(address);
  }
}
