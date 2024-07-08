package com.chat.user_service.service;

import com.chat.user_service.entity.User;
import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.repository.UserRepository;
import com.chat.user_service.server.model.*;
import com.chat.user_service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserAddressService userAddressService;

  public Mono<ResponseEntity<GetUserProfile200Response>> getUserProfile(String userId) {
    return userRepository.findById(userId)
            .map(Utils::convertUserToUserProfile)
            .flatMap(userProfile -> {
              return userAddressService.getUserAddress(userProfile.getUserId())
                      .map(Utils::convertUserAddressToUserProfileAddress)
                      .map(userProfileAddress -> {
                        userProfile.setAddress(userProfileAddress);
                        return userProfile;
                      });
            })
            .map(userProfile -> {
              GetUserProfile200Response response = new GetUserProfile200Response();
              response.setData(userProfile);
              response.setMessage("User profile retrieved successfully");
              return ResponseEntity.ok(response);
            });
  }

  public Mono<User> getUser(String userId) {
    return userRepository.findById(userId);
  }

  public Mono<ResponseEntity<UpdateUserProfile200Response>> updateUserProfile(Mono<UpdateProfileRequest> updateProfileRequest) {
    return Mono.zip(updateProfileRequest, userRepository.findById("test"), userAddressService.getUserAddress("test"))
            .flatMap(t3 -> {
              UpdateProfileRequest request = t3.getT1();
              User user = t3.getT2();
              UserAddress userAddress = t3.getT3();


              // set display name if the new display name is not null and different from current display name
              if (Objects.nonNull(request.getDisplayName()) && !Objects.equals(request.getDisplayName(), request.getDisplayName())) {
                user.setDisplayName(request.getDisplayName());
              }

              UserProfileAddress address = request.getAddress();

              // set address if the new address is not null and different from current address
              if (Objects.nonNull(address)) {
                if (Objects.nonNull(address.getCountry()) && !Objects.equals(address.getCountry(), userAddress.getCountry())) {
                  userAddress.setCountry(address.getCountry());
                }

                if (Objects.nonNull(address.getCity()) && !Objects.equals(address.getCity(), userAddress.getCity())) {
                  userAddress.setCity(address.getCity());
                }

                if (Objects.nonNull(address.getDistrict()) && !Objects.equals(address.getDistrict(), userAddress.getDistrict())) {
                  userAddress.setDistrict(address.getDistrict());
                }

                if (Objects.nonNull(address.getProvince()) && !Objects.equals(address.getProvince(), userAddress.getProvince())) {
                  userAddress.setProvince(address.getProvince());
                }

                if (Objects.nonNull(address.getWard()) && !Objects.equals(address.getWard(), userAddress.getWard())) {
                  userAddress.setWard(address.getWard());
                }


              }

              return Mono.zip(userRepository.save(user), userAddressService.save(userAddress)) ;
            })
            .then(Mono.just(ResponseEntity.ok().body(new UpdateUserProfile200Response())));

  }

}
