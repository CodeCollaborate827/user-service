package com.chat.user_service.delegator;

import com.chat.user_service.api.UserApiController;
import com.chat.user_service.exception.ApplicationExceptionHandler;
import com.chat.user_service.model.*;
import com.chat.user_service.service.FriendshipService;
import com.chat.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.chat.user_service.utls.ApiTestUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(controllers = UserApiDelegatorImpl.class)
@ContextConfiguration(classes = {UserApiDelegatorImpl.class, UserApiController.class, ApplicationExceptionHandler.class})
class UserApiDelegatorImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private FriendshipService friendshipService;


    @Test
    public void acceptFriendRequest_withValidInput_shouldReturnSuccessResponse() {
        // given
        CommonSuccessResponse commonResponse = getCommonSuccessResponse();
        when(friendshipService.acceptFriendRequest(any(), anyString(), any(Mono.class)))
                .thenReturn(Mono.just(ResponseEntity.ok(commonResponse)));

        // when
        AcceptFriendRequest acceptFriendRequest = getAcceptFriendRequest();
        WebTestClient.ResponseSpec response = webTestClient.post().uri(ACCEPT_FRIEND_REQUEST_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(acceptFriendRequest), AcceptFriendRequest.class)
                .headers(setUserIdAndRequestIdInRequestHeader())
                .exchange();


        // then
        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath(COMMON_RESPONSE_ERROR_CODE_JSON_PATH).doesNotExist()
                .jsonPath(COMMON_RESPONSE_MESSAGE_JSON_PATH).isEqualTo(getCommonSuccessResponse().getMessage())
                .jsonPath(COMMON_RESPONSE_REQUEST_ID_JSON_PATH).isEqualTo(getCommonSuccessResponse().getRequestId());

    }

    @Test
    public void acceptFriendRequest_withValidInputAndWithoutRequestIdHeader_shouldReturnSuccessResponse() {
        // given
        CommonSuccessResponse commonResponse = getCommonSuccessResponse();

        when(friendshipService.acceptFriendRequest(any(), anyString(), any(Mono.class)))
                .thenReturn(Mono.just(ResponseEntity.ok(commonResponse)));

        // when
        AcceptFriendRequest acceptFriendRequest = getAcceptFriendRequest();
        WebTestClient.ResponseSpec response = webTestClient.post().uri(ACCEPT_FRIEND_REQUEST_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(acceptFriendRequest), AcceptFriendRequest.class)
                .headers(setUserIdInRequestHeader())
                .exchange();

        // then

        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath(COMMON_RESPONSE_REQUEST_ID_JSON_PATH).exists()
                .jsonPath(COMMON_RESPONSE_MESSAGE_JSON_PATH).isEqualTo(getCommonSuccessResponse().getMessage())
                .jsonPath(COMMON_RESPONSE_ERROR_CODE_JSON_PATH).doesNotExist();
    }

    @Test
    public void acceptFriendRequest_withValidInputAndWithoutUserIdHeader_shouldReturnBadRequest() {

        // when
        AcceptFriendRequest acceptFriendRequest = getAcceptFriendRequest();
        WebTestClient.ResponseSpec response = webTestClient.post().uri(ACCEPT_FRIEND_REQUEST_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(acceptFriendRequest), AcceptFriendRequest.class)
                .headers(setRequestIdInRequestHeader())
                .exchange();

        // then

        response.expectStatus().is4xxClientError();
    }

    @Test
    public void denyFriendRequest_withValidInput_shouldReturnSuccessResponse() {
        // given
        CommonSuccessResponse commonResponseMono = getCommonSuccessResponse();
        when(friendshipService.denyFriendRequest(any(), any(), any()))
                .thenReturn(Mono.just(ResponseEntity.ok(commonResponseMono)));

        // when
        DenyFriendRequest denyFriendRequest = getDenyFriendRequest();
        WebTestClient.ResponseSpec response = webTestClient.post().uri(DENY_FRIEND_REQUEST_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(denyFriendRequest), DenyFriendRequest.class)
                .headers(setUserIdAndRequestIdInRequestHeader())
                .exchange();


        // verify

        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath(COMMON_RESPONSE_ERROR_CODE_JSON_PATH).doesNotExist()
                .jsonPath(COMMON_RESPONSE_MESSAGE_JSON_PATH).isEqualTo(getCommonSuccessResponse().getMessage())
                .jsonPath(COMMON_RESPONSE_REQUEST_ID_JSON_PATH).isEqualTo(getCommonSuccessResponse().getRequestId());
    }


    @Test
    public void denyFriendRequest_withValidInputAndWithoutRequestIdHeader_shouldReturnSuccessResponse() {
        // given
        CommonSuccessResponse commonResponseMono = getCommonSuccessResponse();
        when(friendshipService.denyFriendRequest(any(), any(), any()))
                .thenReturn(Mono.just(ResponseEntity.ok(commonResponseMono)));

        // when
        DenyFriendRequest denyFriendRequest = getDenyFriendRequest();
        WebTestClient.ResponseSpec response = webTestClient.post().uri(DENY_FRIEND_REQUEST_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(denyFriendRequest), DenyFriendRequest.class)
                .headers(setUserIdInRequestHeader())
                .exchange();

        // verify
        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath(COMMON_RESPONSE_REQUEST_ID_JSON_PATH).isEqualTo(getCommonSuccessResponse().getRequestId())
                .jsonPath(COMMON_RESPONSE_MESSAGE_JSON_PATH).isEqualTo(getCommonSuccessResponse().getMessage())
                .jsonPath(COMMON_RESPONSE_ERROR_CODE_JSON_PATH).doesNotExist();

    }

    @Test
    public void denyFriendRequest_withValidInputAndWithoutUserIdHeader_shouldReturnBadRequest() {

        // when
        DenyFriendRequest denyFriendRequest = getDenyFriendRequest();
        WebTestClient.ResponseSpec response = webTestClient.post().uri(DENY_FRIEND_REQUEST_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(denyFriendRequest), DenyFriendRequest.class)
                .headers(setRequestIdInRequestHeader())
                .exchange();

        // verify
        response.expectStatus().is4xxClientError();
    }

    @Test
    public void getUserFriendRequests_withValidInput_shouldReturnSuccessResponse() {
        // given
        FriendRequestListPagingResponse friendRequestListPagingResponse = getFriendRequestListPagingResponse();
        when(friendshipService.getUserFriendRequests(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(ResponseEntity.ok(friendRequestListPagingResponse)));

        // when
        WebTestClient.ResponseSpec response = webTestClient.get().uri(GET_FRIEND_REQUESTS_ENDPOINT.formatted(5, 1))
                .accept(APPLICATION_JSON)
                .headers(setUserIdAndRequestIdInRequestHeader())
                .exchange();

        // verify
        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath(COMMON_RESPONSE_MESSAGE_JSON_PATH).isEqualTo(getCommonSuccessResponse().getMessage())
                .jsonPath(COMMON_RESPONSE_REQUEST_ID_JSON_PATH).isEqualTo(getCommonSuccessResponse().getRequestId())
                .jsonPath(COMMON_RESPONSE_ERROR_CODE_JSON_PATH).doesNotExist()
                .jsonPath(COMMON_RESPONSE_DATA_JSON_PATH).exists();
    }

    @Test
    public void getUserFriendRequests_withValidInputAndWithoutRequestIdHeader_shouldReturnSuccessResponse() {
        // given
        FriendRequestListPagingResponse friendRequestListPagingResponse = getFriendRequestListPagingResponse();
        when(friendshipService.getUserFriendRequests(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(ResponseEntity.ok(friendRequestListPagingResponse)));

        // when
        WebTestClient.ResponseSpec response = webTestClient.get().uri(GET_FRIEND_REQUESTS_ENDPOINT.formatted(5, 1))
                .accept(APPLICATION_JSON)
                .headers(setUserIdInRequestHeader())
                .exchange();

        // verify

        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath(COMMON_RESPONSE_ERROR_CODE_JSON_PATH).doesNotExist()
                .jsonPath(COMMON_RESPONSE_REQUEST_ID_JSON_PATH).isEqualTo(getCommonSuccessResponse().getRequestId())
                .jsonPath(COMMON_RESPONSE_MESSAGE_JSON_PATH).isEqualTo(getCommonSuccessResponse().getMessage())
                .jsonPath(COMMON_RESPONSE_DATA_JSON_PATH).exists();
    }

    @Test
    public void getUserFriendRequests_withValidInputAndWithoutUserIdHeader_shouldReturnBadRequestResponse() {
        // given
        FriendRequestListPagingResponse friendRequestListPagingResponse = getFriendRequestListPagingResponse();
        when(friendshipService.getUserFriendRequests(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(ResponseEntity.ok(friendRequestListPagingResponse)));

        // when
        WebTestClient.ResponseSpec response = webTestClient.get().uri(GET_FRIEND_REQUESTS_ENDPOINT.formatted(5, 1))
                .accept(APPLICATION_JSON)
                .headers(setRequestIdInRequestHeader())
                .exchange();

        // verify

        response.expectStatus().is4xxClientError();
    }

    @Test
    public void getUserFriends_withValidInput_shouldReturnSuccessResponse() {
        // given
        FriendsListPagingResponse friendsListPagingResponse = getFriendsListPagingResponse();
        when(userService.getUserFriends(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(ResponseEntity.ok(friendsListPagingResponse)));

        // when
        WebTestClient.ResponseSpec response = webTestClient.get().uri(GET_FRIEND_LIST_ENDPOINT.formatted(5, 1))
                .headers(setUserIdAndRequestIdInRequestHeader())
                .accept(APPLICATION_JSON)
                .exchange();

        // verify
        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath(COMMON_RESPONSE_REQUEST_ID_JSON_PATH).isEqualTo(getCommonSuccessResponse().getRequestId())
                .jsonPath(COMMON_RESPONSE_MESSAGE_JSON_PATH).isEqualTo(getCommonSuccessResponse().getMessage())
                .jsonPath(COMMON_RESPONSE_ERROR_CODE_JSON_PATH).doesNotExist()
                .jsonPath(COMMON_RESPONSE_DATA_JSON_PATH).exists();
    }

    @Test
    public void getUserFriends_withValidInputAndWithoutRequestIdHeader_shouldReturnSuccessResponse() {
        // given
        FriendsListPagingResponse friendsListPagingResponse = getFriendsListPagingResponse();

        when(userService.getUserFriends(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(ResponseEntity.ok(friendsListPagingResponse)));

        // when
        WebTestClient.ResponseSpec response = webTestClient.get().uri(GET_FRIEND_LIST_ENDPOINT.formatted(5, 1))
                .accept(APPLICATION_JSON)
                .headers(setUserIdInRequestHeader())
                .exchange();

        // verify
        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath(COMMON_RESPONSE_REQUEST_ID_JSON_PATH).exists()
                .jsonPath(COMMON_RESPONSE_MESSAGE_JSON_PATH).isEqualTo(getCommonSuccessResponse().getMessage())
                .jsonPath(COMMON_RESPONSE_ERROR_CODE_JSON_PATH).doesNotExist()
                .jsonPath(COMMON_RESPONSE_DATA_JSON_PATH).exists();
    }

    @Test
    public void getUserFriends_withValidInputAndWithoutUserIdHeader_shouldReturnBadRequestResponse() {

        // when
        WebTestClient.ResponseSpec response = webTestClient.get().uri(GET_FRIEND_LIST_ENDPOINT.formatted(5, 1))
                .headers(setRequestIdInRequestHeader())
                .accept(APPLICATION_JSON)
                .exchange();

        // verify
        response.expectStatus().is4xxClientError();

    }

    @Test
    public void getUserProfile_withValidInput_shouldReturnSuccessResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void getUserProfile_withValidInputAndWithoutRequestIdHeader_shouldReturnSuccessResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void getUserProfile_withValidInputAndWithoutUserIdHeader_shouldReturnBadRequestResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void sendFriendRequest_withValidInput_shouldReturnSuccessResponse() {
        // given

        // when

        // verify
    }


    @Test
    public void sendFriendRequest_withValidInputAndWithoutRequestIdHeader_shouldReturnSuccessResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void sendFriendRequest_withValidInputAndWithoutUserIdHeader_shouldReturnBadRequestResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void updateUserProfile_withValidInput_shouldReturnSuccessResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void updateUserProfile_withValidInputAndWithoutRequestIdHeader_shouldReturnSuccessResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void updateUserProfile_withValidInputAndWithoutUserIdHeader_shouldReturnBadRequestResponse() {
        // given

        // when

        // verify
    }


    @Test
    public void updateUserProfileImage_withValidInput_shouldReturnSuccessResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void updateUserProfileImage_withValidInputAndWithoutRequestIdHeader_shouldReturnSuccessResponse() {
        // given

        // when

        // verify
    }

    @Test
    public void updateUserProfileImage_withValidInputAndWithoutUserIdHeader_shouldReturnBadRequestResponse() {
        // given

        // when

        // verify
    }

}