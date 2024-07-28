package com.chat.user_service.service;

import com.chat.user_service.entity .FriendRequest;
import com.chat.user_service.entity.FriendShip;
import com.chat.user_service.exception.ApplicationException;
import com.chat.user_service.exception.ErrorCode;
import com.chat.user_service.model.*;
import com.chat.user_service.repository.FriendRequestRepository;
import com.chat.user_service.repository.FriendshipRepository;
import com.chat.user_service.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {

  private final FriendshipRepository friendshipRepository;

  private final FriendRequestRepository friendRequestRepository;

  private final UserService userService;

  @Value("${test_user_id}")
  private UUID userId;

  public Mono<ResponseEntity<FriendRequestListPagingResponse>> getUserFriendRequests(int pageSize, int currentPage) {
    log.info("Fetching friend requests of user: {}", userId);
    //TODO: check user exists,
    return friendRequestRepository.countByRecipientIdAndStatus(userId, FriendRequest.Status.PENDING)
            .flatMap(count -> {
              log.info("count: {}", count);
              //TODO: this is similar to getFriends, try to generalize it
              FriendRequestListPagingResponseData friendRequestData = new FriendRequestListPagingResponseData();
              friendRequestData.setTotalItems(count);
              friendRequestData.setCurrentPage(currentPage);
              friendRequestData.setPageSize(pageSize);

              int totalPages = (int) Math.ceil((double) count / pageSize);
              friendRequestData.setTotalPages(totalPages);

              int offset = (currentPage - 1) * pageSize;
              int limit =  pageSize;

              return friendRequestRepository.findFriendRequestOfUserPaging(userId, offset, limit, FriendRequest.Status.PENDING)
                      .collectList()
                      .map(friendRequestDTOList -> {
                        friendRequestData.setItems(friendRequestDTOList);
                        return friendRequestData;
                      })
                      .map(data -> {
                        FriendRequestListPagingResponse response = new FriendRequestListPagingResponse();
                        response.setData(data);
                        response.setMessage("Get friend requests successfully"); // TODO: move it to constant
                        response.setRequestId(UUID.randomUUID().toString()); //  TODO: get the id from header
                        return ResponseEntity.ok(response);

                      });
            });
  }

  public Mono<ResponseEntity<CommonSuccessResponse>> sendFriendRequest(Mono<AddFriendRequest> addFriendRequest) {
    UUID senderId = userId;
    // check recipient exists
    //TODO: please clean this code
    return addFriendRequest.flatMap(request -> userService.getUserById(Utils.convertStringToUUID(request.getUserId())))
            .switchIfEmpty(Mono.error(new ApplicationException(ErrorCode.USER_ERROR1)))
            // check if 2 users were already friends
            .flatMap(recipient -> {
              return friendshipRepository.findFriendShipBetween2Users(senderId, recipient.getId())
                      .flatMap(friendship -> Mono.error(new ApplicationException(ErrorCode.USER_ERROR2)))
                      .switchIfEmpty(Mono.just(recipient))
                      // check if sender has already sent the friend request
                      .flatMap(r -> {
                        return friendRequestRepository.findBySenderIdAndRecipientIdAndStatus(senderId, recipient.getId(), FriendRequest.Status.PENDING)
                                .flatMap(friendRequest -> Mono.error(new ApplicationException(ErrorCode.USER_ERROR3)))
                                .switchIfEmpty(Mono.just(recipient));
                      })
                      // check if the current recipient has sent another request to current sender
                      .flatMap(friendRequest ->
                              friendRequestRepository.findBySenderIdAndRecipientIdAndStatus(
                                      recipient.getId(),
                                      senderId,
                                      FriendRequest.Status.PENDING)
                      )
                      .flatMap(friendRequestFromRecipient -> {
                        // if there was another friend request from the recipient to the current sender
                        // when the sender send friend request to the recipient,
                        // it means the sender accepts the friend request of the recipient
                        friendRequestFromRecipient.setStatus(FriendRequest.Status.ACCEPTED);
                        friendRequestFromRecipient.setUpdatedAt(OffsetDateTime.now());
                        return friendRequestRepository.save(friendRequestFromRecipient)
                                .then(Mono.just(Utils.createSuccessResponse("Accepted friend request successfully")));
                      })
                      // when there is no another request from the recipient to the sender
                      .switchIfEmpty(
                              Mono.just(createNewFriendRequest(senderId, recipient.getId()))
                                      .flatMap(friendRequestRepository::save)
                                      .then(Mono.just(Utils.createSuccessResponse("Friend request sent successfully")))
                      );

                      });




  }


  private FriendRequest createNewFriendRequest(UUID senderId, UUID recipientId) {
    FriendRequest friendRequest = new FriendRequest();
    friendRequest.setSenderId(senderId);
    friendRequest.setRecipientId(recipientId);
    friendRequest.setStatus(FriendRequest.Status.PENDING);

    return friendRequest;
  }

  public Mono<ResponseEntity<CommonSuccessResponse>> acceptFriendRequest(Mono<AcceptFriendRequest> acceptFriendRequest) {
    UUID recipientId = userId;

    return acceptFriendRequest.flatMap(request -> friendRequestRepository.findById(UUID.fromString(request.getRequestId())))
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

  private ErrorCode checkIfAbleToModifyFriendRequest(FriendRequest friendRequest, UUID recipientUUID) {
    // check if user is the recipient
    if (!Objects.equals(recipientUUID, friendRequest.getRecipientId())) {
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

  private FriendShip createNewFriendShip(UUID user1Id, UUID user2Id) {
    FriendShip friendShip = new FriendShip();
    friendShip.setUser1Id(user1Id);
    friendShip.setUser2Id(user2Id);

    return friendShip;
  }

  public Mono<ResponseEntity<CommonSuccessResponse>> denyFriendRequest(Mono<DenyFriendRequest> DenyFriendRequest) {
    UUID currentUserId = userId;

    return DenyFriendRequest.flatMap(request -> friendRequestRepository.findById(UUID.fromString(request.getRequestId())))
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
