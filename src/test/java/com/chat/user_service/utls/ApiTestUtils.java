package com.chat.user_service.utls;

import com.chat.user_service.model.*;
import org.springframework.http.HttpHeaders;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ApiTestUtils {
    public static final String ADD_FRIEND_REQUEST_ENDPOINT = "/api/user/friends/add-friend";
    public static final String ACCEPT_FRIEND_REQUEST_ENDPOINT = "/api/user/friends/accept-friend-request";
    public static final String DENY_FRIEND_REQUEST_ENDPOINT = "/api/user/friends/deny-friend-request";
    public static final String GET_FRIEND_REQUESTS_ENDPOINT = "/api/user/friends/friend-requests?pageSize=%d&currentPage=%d";
    public static final String GET_FRIEND_LIST_ENDPOINT = "/api/user/friends?pageSize=%d&currentPage=%d";
    public static final String UPDATE_USER_AVATAR = "/api/user/profile/image";
    public static final String UPDATE_USER_PROFILE = "/api/user/profile";
    public static final String GET_USER_PROFILE = "/api/user/profile";


    private static final String REQUEST_ID_HEADER = "requestId";
    private static final String USER_ID_HEADER = "userId";
    public static final String VALID_REQUEST_ID = UUID.randomUUID().toString();
    public static final UUID VALID_USER_ID = UUID.randomUUID();


    public static final String COMMON_RESPONSE_MESSAGE_JSON_PATH = "$.message";
    public static final String COMMON_RESPONSE_REQUEST_ID_JSON_PATH = "$.requestId";
    public static final String COMMON_RESPONSE_ERROR_CODE_JSON_PATH = "$.errorCode";
    public static final String COMMON_RESPONSE_DATA_JSON_PATH = "$.data";


    public static final String SUCCESS_RESPONSE_MESSAGE = "OPERATION SUCCESSFUL!";
    public static final String STRING_CONTENT = "string content";

    public static CommonSuccessResponse getCommonSuccessResponse() {
        CommonSuccessResponse response = new CommonSuccessResponse();
        response.setRequestId(VALID_REQUEST_ID);
        response.setMessage(SUCCESS_RESPONSE_MESSAGE);
        return response;
    }

    public static FriendRequestListPagingResponse getFriendRequestListPagingResponse() {
        FriendRequestListPagingResponse response = new FriendRequestListPagingResponse();
        response.setRequestId(VALID_REQUEST_ID);
        response.setMessage(SUCCESS_RESPONSE_MESSAGE);

        FriendRequestListPagingResponseData responseData = new FriendRequestListPagingResponseData();
        responseData.setCurrentPage(1);
        responseData.setPageSize(5);
        responseData.setTotalPages(1);
        responseData.setTotalItems(2);

        List<FriendRequestDTO> items = new ArrayList<>();
        items.add(new FriendRequestDTO());
        items.add(new FriendRequestDTO());

        responseData.setItems(items);
        response.setData(responseData);

        return response;
    }

    public static FriendsListPagingResponse getFriendsListPagingResponse() {
        FriendsListPagingResponse response = new FriendsListPagingResponse();
        response.setRequestId(VALID_REQUEST_ID);
        response.setMessage(SUCCESS_RESPONSE_MESSAGE);

        FriendsListPagingResponseData responseData = new FriendsListPagingResponseData();
        responseData.setCurrentPage(1);
        responseData.setPageSize(5);
        responseData.setTotalPages(1);
        responseData.setTotalItems(2);

        List<FriendDTO> items = new ArrayList<>();
        items.add(new FriendDTO());
        items.add(new FriendDTO());

        responseData.setItems(items);
        response.setData(responseData);
        return response;
    }


    public static UserProfileResponse getUserProfileResponse() {
        UserProfileResponse response = new UserProfileResponse();
        response.setMessage(SUCCESS_RESPONSE_MESSAGE);
        response.setRequestId(VALID_REQUEST_ID);

        UserProfileResponseData responseData = new UserProfileResponseData();
        responseData.setAddress(new UserProfileResponseDataAddress());
        responseData.setUserId(VALID_USER_ID.toString());
        responseData.setDisplayName(STRING_CONTENT);
        responseData.setUpdatedAt(OffsetDateTime.now().toString());
        responseData.setEmail(STRING_CONTENT);
        responseData.setCreatedAt(OffsetDateTime.now().toString());
        responseData.setUsername(STRING_CONTENT);
        responseData.setAvatarUrl(STRING_CONTENT);

        response.setData(responseData);

        return response;
    }

    public static AddFriendRequest getAddFriendRequest() {
        AddFriendRequest addFriendRequest = new AddFriendRequest();
        addFriendRequest.setUserId(VALID_USER_ID.toString());
        return addFriendRequest;
    }

    public static UpdateProfileRequest getUpdateProfileRequest() {
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();

        updateProfileRequest.setAddress(new UserProfileResponseDataAddress());
        updateProfileRequest.setDisplayName(STRING_CONTENT);

        return updateProfileRequest;
    }




    public static AcceptFriendRequest getAcceptFriendRequest() {
        AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest();
        acceptFriendRequest.setRequestId(UUID.randomUUID().toString());

        return acceptFriendRequest;
    }

    public static DenyFriendRequest getDenyFriendRequest() {
        DenyFriendRequest acceptFriendRequest = new DenyFriendRequest();
        acceptFriendRequest.setRequestId(UUID.randomUUID().toString());

        return acceptFriendRequest;
    }

    public static Consumer<HttpHeaders> setUserIdAndRequestIdInRequestHeader() {
        return httpHeaders -> {
            setUserIdInRequestHeader()
                    .andThen(setRequestIdInRequestHeader())
                    .accept(httpHeaders);
        };
    }

    public static Consumer<HttpHeaders> setUserIdInRequestHeader() {
        return httpHeaders -> {
            httpHeaders.set(USER_ID_HEADER, VALID_USER_ID.toString());
        };
    }

    public static Consumer<HttpHeaders> setRequestIdInRequestHeader() {
        return httpHeaders -> {
            httpHeaders.set(REQUEST_ID_HEADER, VALID_REQUEST_ID);
        };
    }

}
