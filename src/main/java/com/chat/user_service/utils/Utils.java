package com.chat.user_service.utils;

import com.chat.user_service.entity.User;
import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.server.model.UserProfile;
import com.chat.user_service.server.model.UserProfileAddress;

import java.time.OffsetDateTime;

public class Utils {

  public static UserProfile convertUserToUserProfile(User user) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserId(user.getId());
    userProfile.setEmail(user.getEmail());
    userProfile.setDisplayName(user.getDisplayName());
    userProfile.setUsername(user.getUsername());
    userProfile.setAvatarUrl(user.getAvatarUrl());
    userProfile.setCreatedAt(OffsetDateTime.of(user.getCreatedAt(), OffsetDateTime.now().getOffset()));
    userProfile.setUpdatedAt(OffsetDateTime.of(user.getUpdatedAt(), OffsetDateTime.now().getOffset()));

    return userProfile;
  }

  public static UserProfileAddress convertUserAddressToUserProfileAddress(UserAddress userAddress) {
    UserProfileAddress userProfileAddress = new UserProfileAddress();
    userProfileAddress.setCountry(userAddress.getCountry());
    userProfileAddress.setCity(userAddress.getCity());
    userProfileAddress.district(userAddress.getDistrict());
    userProfileAddress.setProvince(userAddress.getProvince());
    userProfileAddress.setWard(userAddress.getWard());

    return userProfileAddress;
  }

}
