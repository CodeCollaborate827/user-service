package com.chat.user_service.service;

import com.chat.user_service.entity.User;
import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.exception.ApplicationException;
import com.chat.user_service.exception.ErrorCode;
import com.chat.user_service.model.CommonSuccessResponse;
import com.chat.user_service.model.UpdateProfileRequest;
import com.chat.user_service.model.UserProfileResponse;
import com.chat.user_service.model.UserProfileResponseAddress;
import com.chat.user_service.repository.UserRepository;
import com.chat.user_service.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserAddressService userAddressService;

  public Mono<ResponseEntity<UserProfileResponse>> getUserProfile(String userId) {
    // get the user
    return getUserById(userId)
            .map(user -> {
              UserProfileResponse userProfileResponse = Utils.convertUserToUserProfile(user);
              UserProfileResponseAddress userProfileResponseAddress = Utils.convertUserAddressToUserProfileAddress(user.getAddress());
              userProfileResponse.setAddress(userProfileResponseAddress);
              return userProfileResponse;
            })
            .map(ResponseEntity.ok()::body)
            .switchIfEmpty(Mono.error(new ApplicationException(ErrorCode.USER_ERROR1)));
  }

  private Mono<User> getUserById(String userId) {
    // get the user and the user address (if the user address is not present, use default empty address value)
    return Mono.zip(userRepository.findById(userId), userAddressService.getUserAddress(userId).switchIfEmpty(Mono.just(new UserAddress())))
            .map(t2 -> {
              User user = t2.getT1();
              UserAddress userAddress = t2.getT2();
              user.setAddress(userAddress);
              return user;
            });
  }

  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfile(Mono<UpdateProfileRequest> updateProfileRequest, String userId) {
    return getUserById(userId).zipWith(updateProfileRequest)
            .flatMap(t2 -> {
              User user = t2.getT1();
              UpdateProfileRequest request = t2.getT2();

              log.info("User:{}", user);
              // update the user display name
              if (Objects.nonNull(request.getDisplayName())) {
                log.info("Updating user last name for user id: {}", user.getId());
                user.setDisplayName(request.getDisplayName());
                user.setUpdatedAt(LocalDateTime.now());
              }

              UserAddress userAddress = user.getAddress();
              if (Objects.nonNull(request.getAddress())) {
                UserProfileResponseAddress newAddress = request.getAddress();
                log.info("Updating user address for user id: {}", user.getId());

                // update the user address
                if (Objects.nonNull(newAddress.getCountry())) userAddress.setCountry(newAddress.getCountry());
                if (Objects.nonNull(newAddress.getProvince())) userAddress.setProvince(newAddress.getProvince());
                if (Objects.nonNull(newAddress.getCity())) userAddress.setCity(newAddress.getCity());
                if (Objects.nonNull(newAddress.getDistrict())) userAddress.setDistrict(newAddress.getDistrict());
                if (Objects.nonNull(newAddress.getWard())) userAddress.setWard(newAddress.getWard());

                userAddress.setUpdatedAt(LocalDateTime.now());
              }

              // save the user and the user address
              return userRepository.save(user)
                      .then(userAddressService.save(userAddress));

            })
            .then(Mono.just(Utils.createSuccessResponse("User profile updated successfully")))
            .switchIfEmpty(Mono.error(new ApplicationException(ErrorCode.USER_ERROR1)));
  }




}
