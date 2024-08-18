package com.chat.user_service.delegator;

import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/fetch")
public class TestController {

  private final UserAddressRepository userAddressRepository;

  @GetMapping
  public Mono<UserAddress> getUserAddress() {
    return userAddressRepository.findByUserId(UUID.fromString("0554c6a6-12de-4b35-8a4a-197794179f47"));
  }
}
