package com.chat.user_service.utils;

import com.chat.user_service.entity.User;
import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.model.CommonSuccessResponse;
import com.chat.user_service.model.Friend;
import com.chat.user_service.model.UserProfileResponse;
import com.chat.user_service.model.UserProfileResponseAddress;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

public class Utils {

  public static UserProfileResponse convertUserToUserProfile(User user) {
    UserProfileResponse userProfile = new UserProfileResponse();
    userProfile.setUserId(user.getId());
    userProfile.setEmail(user.getEmail());
    userProfile.setDisplayName(user.getDisplayName());
    userProfile.setUsername(user.getUsername());
    userProfile.setAvatarUrl(user.getAvatarUrl());
    userProfile.setCreatedAt(OffsetDateTime.of(user.getCreatedAt(), OffsetDateTime.now().getOffset()));
    userProfile.setUpdatedAt(OffsetDateTime.of(user.getUpdatedAt(), OffsetDateTime.now().getOffset()));

    return userProfile;
  }


  public static UserProfileResponseAddress convertUserAddressToUserProfileAddress(UserAddress userAddress) {
    UserProfileResponseAddress userProfileResponseAddress = new UserProfileResponseAddress();
    userProfileResponseAddress.setCountry(userAddress.getCountry());
    userProfileResponseAddress.setCity(userAddress.getCity());
    userProfileResponseAddress.setProvince(userAddress.getProvince());
    userProfileResponseAddress.setDistrict(userAddress.getDistrict());
    userProfileResponseAddress.setWard(userAddress.getWard());

    return userProfileResponseAddress;
  }

  public static ResponseEntity<CommonSuccessResponse> createSuccessResponse(String message) {
    CommonSuccessResponse response = new CommonSuccessResponse();
    response.setMessage(message);


    return ResponseEntity.ok(response);
  }

  public static Friend convertUserToFriend(User user) {
    Friend friend = new Friend();
    friend.setUserId(user.getId());
    friend.setUsername(user.getUsername());
    friend.setDisplayName(user.getDisplayName());
    friend.setAvatarUrl(user.getAvatarUrl());
    friend.setEmail(user.getEmail());

    return friend;
  }



}
