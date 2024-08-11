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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final FriendshipRepository friendshipRepository;
  private final UserRepository userRepository;
  private final UserAddressService userAddressService;

  @Value("${test_user_id}")
  private UUID testUserId;

  public Mono<ResponseEntity<UserProfileResponse>> getUserProfile() {
    UUID userId = testUserId;
    // get the user
    return getUserById(userId)
            .map(user -> {
              UserProfileResponseData userProfileResponse = Utils.convertUserToUserProfile(user);
              UserProfileResponseDataAddress userProfileResponseAddress = Utils.convertUserAddressToUserProfileAddress(user.getAddress());
              userProfileResponse.setAddress(userProfileResponseAddress);
              return userProfileResponse;
            })
            .map(data -> {
              UserProfileResponse response = new UserProfileResponse();
              response.setData(data);
              response.setMessage("Get user profile successfully"); // TODO: move it to env constant
              response.setRequestId(UUID.randomUUID().toString()); // TODO: get the id from header
              return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.error(new ApplicationException(ErrorCode.USER_ERROR1)));
  }

  public Mono<User> getUserById(UUID userId) {
    // get the user and the user address (if the user address is not present, use default empty address value)
    return Mono.zip(userRepository.findById(userId), userAddressService.getUserAddress(userId).switchIfEmpty(Mono.just(new UserAddress())))
            .map(t2 -> {
              User user = t2.getT1();
              UserAddress userAddress = t2.getT2();
              user.setAddress(userAddress);
              return user;
            });
  }

  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfile(Mono<UpdateProfileRequest> updateProfileRequest) {
    UUID userId = testUserId;
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
                user.setUpdatedAt(OffsetDateTime.now());
              }

              UserAddress userAddress = user.getAddress();
              if (Objects.nonNull(request.getAddress())) {
                UserProfileResponseDataAddress newAddress = request.getAddress();
                log.info("Updating user address for user id: {}", user.getId());

                // update the user address
                if (Objects.nonNull(newAddress.getCountry())) userAddress.setCountry(newAddress.getCountry());
                if (Objects.nonNull(newAddress.getProvince())) userAddress.setProvince(newAddress.getProvince());
                if (Objects.nonNull(newAddress.getCity())) userAddress.setCity(newAddress.getCity());
                if (Objects.nonNull(newAddress.getDistrict())) userAddress.setDistrict(newAddress.getDistrict());
                if (Objects.nonNull(newAddress.getWard())) userAddress.setWard(newAddress.getWard());

                userAddress.setUpdatedAt(OffsetDateTime.now());
              }

              // save the user and the user address
              return userRepository.save(user)
                      .then(userAddressService.save(userAddress));

            })
            .then(Mono.just(Utils.createSuccessResponse("User profile updated successfully")))
            .switchIfEmpty(Mono.error(new ApplicationException(ErrorCode.USER_ERROR1)));
  }


  public Mono<ResponseEntity<FriendsListPagingResponse>> getUserFriends(int pageSize, int currentPage) {
    UUID userId = testUserId;
    // count the total item first
      return friendshipRepository.countByUser1IdOrUser2Id(userId, userId)
              .flatMap(count -> {
                FriendsListPagingResponseData response = new FriendsListPagingResponseData();
                response.setTotalItems(count);
                response.setPageSize(pageSize);
                response.currentPage(currentPage);

                int totalPageNum = (int) Math.ceil((double) count / pageSize);
                response.setTotalPages(totalPageNum);

                int offset = (currentPage - 1) * (pageSize);
                int limit = pageSize;

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
              .map(data -> {
                FriendsListPagingResponse response = new FriendsListPagingResponse();
                response.setData(data);
                response.setMessage("Get user profile successfully"); // TODO: move it to env constant
                response.setRequestId(UUID.randomUUID().toString()); // TODO: get the id from header

                return ResponseEntity.ok(response);
              });

  }

  public Mono<ResponseEntity<CommonSuccessResponse>> updateUserProfileImage(Flux<Part> avatar) {

    UUID userId = testUserId;
    log.info("Updating user profile image for user id: {}", userId);

    return avatar.doOnNext(part -> {
      // save the image to the file system
      log.info("part:{}", part.content());
      part.content().map(dataBuffer -> {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        return bytes;
      }).map(bytes -> {
        // save the image to the file system
        log.info("bytes:{}", bytes);
        return bytes;
      }).subscribe();
    }).then(Mono.just(Utils.createSuccessResponse("OK")));

  }
}
