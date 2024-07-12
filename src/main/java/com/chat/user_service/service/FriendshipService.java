package com.chat.user_service.service;

import com.chat.user_service.entity.FriendRequest;
import com.chat.user_service.entity.FriendShip;
import com.chat.user_service.exception.ApplicationException;
import com.chat.user_service.exception.ErrorCode;
import com.chat.user_service.model.CommonSuccessResponse;
import com.chat.user_service.model.FriendRequestListPagingResponse;
import com.chat.user_service.repository.FriendRequestRepository;
import com.chat.user_service.repository.FriendshipRepository;
import com.chat.user_service.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {

  private final FriendshipRepository friendshipRepository;

  private final FriendRequestRepository friendRequestRepository;

  private final UserService userService;

  public Mono<ResponseEntity<FriendRequestListPagingResponse>> getUserFriendRequests(String userId, int pageSize, int currentPage) {
    //TODO: check user exists,
    return friendRequestRepository.countByRecipientId(userId)
            .flatMap(count -> {
              log.info("count: {}", count);
              //TODO: this is similar to getFriends, try to generalize it
              FriendRequestListPagingResponse response = new FriendRequestListPagingResponse();
              response.setTotalItems(count);
              response.setCurrentPage(currentPage);
              response.setPageSize(pageSize);

              int totalPages = (int) Math.ceil((double) count / pageSize);
              response.setTotalPages(totalPages);

              int offset = (currentPage - 1) * pageSize;
              int limit = offset + pageSize;

              return friendRequestRepository.findFriendRequestOfUserPaging(userId, offset, limit)
                      .collectList()
                      .map(friendRequestDTOList -> {
                        response.setItems(friendRequestDTOList);
                        return response;
                      })
                      .map(ResponseEntity.ok()::body);
            });
  }

  public Mono<ResponseEntity<CommonSuccessResponse>> sendFriendRequest(String senderId, String recipientId) {
    // check recipient exists
    return userService.getUserById(recipientId)
            .switchIfEmpty(Mono.error(new ApplicationException(ErrorCode.USER_ERROR1)))
            // check if 2 users were already friends
            .flatMap(recipient -> {
              return friendshipRepository.findFriendShipBetween2Users(senderId, recipientId)
                      .flatMap(friendship -> Mono.error(new ApplicationException(ErrorCode.USER_ERROR2)))
                      .switchIfEmpty(Mono.just(recipient));
            })
            // check if sender has already sent the friend request
            .flatMap(recipient -> {
              return friendRequestRepository.findBySenderIdAndRecipientIdAndStatus(senderId, recipientId, FriendRequest.Status.PENDING)
                      .flatMap(friendRequest -> Mono.error(new ApplicationException(ErrorCode.USER_ERROR3)))
                      .switchIfEmpty(Mono.just(recipient));
            })
            // check if the current recipient has sent another request to current sender
            .flatMap(friendRequest -> {
              return friendRequestRepository.findBySenderIdAndRecipientIdAndStatus(recipientId, senderId, FriendRequest.Status.PENDING)
                      .flatMap(friendRequestFromRecipient -> {
                        // if there was another friend request from the recipient to the current sender
                        // when the sender send friend request to the recipient,
                        // it means the sender accepts the friend request of the recipient
                        friendRequestFromRecipient.setStatus(FriendRequest.Status.ACCEPTED);
                        friendRequestFromRecipient.setUpdatedAt(OffsetDateTime.now());

                        return friendRequestRepository.save(friendRequestFromRecipient)
                                .then(Mono.just(Utils.createSuccessResponse("Accepted friend request successfully")));
                      });
            })
            // when there is no another request from the recipient to the sender
            .switchIfEmpty(
                    Mono.just(createNewFriendRequest(senderId, recipientId))
                            .flatMap(friendRequestRepository::save)
                            .then(Mono.just(Utils.createSuccessResponse("Friend request sent successfully")))
            );
  }

  private FriendRequest createNewFriendRequest(String senderId, String recipientId) {
    FriendRequest friendRequest = new FriendRequest();
    friendRequest.setSenderId(senderId);
    friendRequest.setRecipientId(recipientId);
    friendRequest.setStatus(FriendRequest.Status.PENDING);

    return friendRequest;
  }

  public Mono<ResponseEntity<CommonSuccessResponse>> acceptFriendRequest(String recipientId, long requestId) {

    return friendRequestRepository.findById(requestId)
            .switchIfEmpty(Mono.error(new ApplicationException(ErrorCode.USER_ERROR4)))
            .flatMap(friendRequest -> {
              ErrorCode errorCode = checkIfAbleToModifyFriendRequest(friendRequest, recipientId);

              if (errorCode != null) {
                return Mono.error(new ApplicationException(errorCode));
              }

              friendRequest.setStatus(FriendRequest.Status.ACCEPTED);
              return friendRequestRepository.save(friendRequest)
                      .then(Mono.just(createNewFriendShip(friendRequest.getSenderId(), recipientId)))
                      .flatMap(friendshipRepository::save)
                      .map(friendship -> Utils.createSuccessResponse("Accepted friend request successfully"));
            });


  }

  private ErrorCode checkIfAbleToModifyFriendRequest(FriendRequest friendRequest, String recipientId) {
    // check if user is the recipient
    if (Objects.equals(recipientId, friendRequest.getRecipientId())) {
      return ErrorCode.USER_ERROR7;
    }

    // check if the request was already accepted
    if (Objects.equals(FriendRequest.Status.ACCEPTED, friendRequest.getStatus())) {
      return ErrorCode.USER_ERROR5;
    }

    // check if the request was already denied
    if (Objects.equals(FriendRequest.Status.DENIED, friendRequest.getStatus())) {
      return ErrorCode.USER_ERROR6;
    }

    return null;
  }

  private FriendShip createNewFriendShip(String user1Id, String user2Id) {
    FriendShip friendShip = new FriendShip();
    friendShip.setUser1Id(user1Id);
    friendShip.setUser2Id(user2Id);

    return friendShip;
  }

  public Mono<ResponseEntity<CommonSuccessResponse>> denyFriendRequest(String currentUserId, long requestId) {
    return friendRequestRepository
            .findById(requestId)
            .flatMap(friendRequest -> {
              ErrorCode errorCode = checkIfAbleToModifyFriendRequest(friendRequest, currentUserId);
              if (errorCode != null) {
                return Mono.error(new ApplicationException(errorCode));
              }

              friendRequest.setStatus(FriendRequest.Status.DENIED);

              return friendRequestRepository.save(friendRequest)
                      .then(Mono.just(Utils.createSuccessResponse("Denied friend request successfully!")));
            });
  }
}
