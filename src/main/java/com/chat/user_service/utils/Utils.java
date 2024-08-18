package com.chat.user_service.utils;

import com.chat.user_service.entity.User;
import com.chat.user_service.entity.UserAddress;
import com.chat.user_service.event.UserRegistrationEvent;
import com.chat.user_service.model.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Utils {


  private static final String DEFAULT_AVATAR_URL = "https://ui-avatars.com/api/?name=%s";
  public static UserProfileResponseData convertUserToUserProfile(User user) {
    UserProfileResponseData userProfile = new UserProfileResponseData();
    userProfile.setUserId(convertUUIDToString(user.getId()));
    userProfile.setEmail(user.getEmail());
    userProfile.setDisplayName(user.getDisplayName());
    userProfile.setUsername(user.getUsername());
    userProfile.setAvatarUrl(user.getAvatarUrl());
    userProfile.setCreatedAt(user.getCreatedAt().toString());
    userProfile.setUpdatedAt(user.getUpdatedAt().toString());

    return userProfile;
  }


  public static UserProfileResponseDataAddress convertUserAddressToUserProfileAddress(UserAddress userAddress) {
    UserProfileResponseDataAddress userProfileResponseAddress = new UserProfileResponseDataAddress();
    userProfileResponseAddress.setCountry(userAddress.getCountry());
    userProfileResponseAddress.setCity(userAddress.getCity());
    userProfileResponseAddress.setProvince(userAddress.getProvince());
    userProfileResponseAddress.setDistrict(userAddress.getDistrict());
    userProfileResponseAddress.setWard(userAddress.getWard());

    return userProfileResponseAddress;
  }

  public static ResponseEntity<CommonSuccessResponse> createSuccessResponse(String message, String requestId) {
    CommonSuccessResponse response = new CommonSuccessResponse();
    response.setMessage(message);
    response.setRequestId(requestId); //TODO: get it from the request header


    return ResponseEntity.ok(response);
  }

  public static FriendDTO convertUserToFriendDTO(User user) {
    FriendDTO friend = new FriendDTO();
    friend.setUserId(convertUUIDToString(user.getId()));
    friend.setUsername(user.getUsername());
    friend.setDisplayName(user.getDisplayName());
    friend.setAvatarUrl(user.getAvatarUrl());
    friend.setEmail(user.getEmail());

    return friend;
  }

  public static User convertToUser(UserRegistrationEvent event) {

    User.Gender gender = User.Gender.valueOf(event.getGender().name());
    OffsetDateTime createdTime = event.getCreatedAt() != null? event.getCreatedAt() : OffsetDateTime.now();
    User user = new User();
    user.setId(convertStringToUUID(event.getUserId()));
    user.setDisplayName(event.getDisplayName());
    user.setUsername(event.getUsername());
    user.setAvatarUrl(event.getAvatar() != null ? event.getAvatar() : generateDefaultAvatar(event.getDisplayName()));
    user.setEmail(event.getEmail());
    user.setDateOfBirth(LocalDate.parse(event.getDateOfBirth()));
    user.setGender(gender);
    user.setCreatedAt(createdTime);
    user.setUpdatedAt(createdTime);

    // construct the user address object
    UserAddress userAddress = new UserAddress();
    userAddress.setCity(event.getCity());
    userAddress.setUserId(user.getId());
    user.setAddress(userAddress);
    return user;
  }

  private static String generateDefaultAvatar(String displayName) {
    return DEFAULT_AVATAR_URL.formatted(displayName.replaceAll("\\w+", "%20")); // replace spaces by ASCII character for space
  }


  public static UUID convertStringToUUID(String id) {
    return UUID.fromString(id);
  }


  public static String convertUUIDToString(UUID uuid) {
    return uuid.toString();
  }


}
