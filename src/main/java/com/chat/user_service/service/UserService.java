package com.chat.user_service.service;

import com.chat.user_service.entity.User;
import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.exception.ApplicationException;
import com.chat.user_service.exception.ErrorCode;
import com.chat.user_service.model.*;
import com.chat.user_service.repository.FriendshipRepository;
import com.chat.user_service.repository.UserRepository;
import com.chat.user_service.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final FriendshipRepository friendshipRepository;
  private final UserRepository userRepository;
  private final UserAddressService userAddressService;

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
    // TODO: refactor this code
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


  public Mono<ResponseEntity<FriendsListPagingResponse>> getUserFriends(String userId, int pageSize, int currentPage) {
     // count the total item first
      return friendshipRepository.countByUser1IdOrUser2Id(userId, userId)
              .flatMap(count -> {
                FriendsListPagingResponse response = new FriendsListPagingResponse();
                response.setTotalItems(count);
                response.setPageSize(pageSize);
                response.currentPage(currentPage);

                int totalPageNum = (int) Math.ceil((double) count / pageSize);
                response.setTotalPages(totalPageNum);

                int offset = (currentPage - 1) * (pageSize);
                int limit = offset + pageSize - 1;

                // fetch user object and convert to Friend dto and set data for the paging response
                return friendshipRepository.findUserFriendsPaging(userId, offset, limit)
                        .collectList()
                        .doOnNext(item -> log.info("user {} ", item))
                        .map(userList ->  userList.stream().map(Utils::convertUserToFriendDTO).toList())
                        .map(friendList -> {
                          response.setItems(friendList);
                          return response;
                        });

              })
              .map(ResponseEntity.ok()::body);

  }
}
