package com.chat.user_service.utls;

import com.chat.user_service.model.AcceptFriendRequest;
import com.chat.user_service.model.CommonSuccessResponse;
import com.chat.user_service.model.DenyFriendRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;

public class ApiTestUtils {
    public static final String ADD_FRIEND_REQUEST_ENDPOINT = "/api/user/friends/add-friend-request";
    public static final String ACCEPT_FRIEND_REQUEST_ENDPOINT = "/api/user/friends/accept-friend-request";
    public static final String DENY_FRIEND_REQUEST_ENDPOINT = "/api/user/friends/deny-friend-request";
    public static final String GET_FRIEND_REQUESTS_ENDPOINT = "/api/user/friends/friend-requests";
    public static final String GET_FRIEND_LIST_ENDPOINT = "/api/user/friends";
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


    public static final String SUCCESS_RESPONSE_MESSAGE = "OPERATION SUCCESSFUL!";
    public static Mono<ResponseEntity<CommonSuccessResponse>> getCommonResponseMono() {

        CommonSuccessResponse successResponse = getCommonSuccessResponse();
        return Mono.just(ResponseEntity.ok(successResponse));
    }

    public static CommonSuccessResponse getCommonSuccessResponse() {
        CommonSuccessResponse response = new CommonSuccessResponse();
        response.setRequestId(VALID_REQUEST_ID);
        response.setMessage(SUCCESS_RESPONSE_MESSAGE);
        return response;
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
