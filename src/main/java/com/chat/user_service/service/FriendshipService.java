package com.chat.user_service.service;

import com.chat.user_service.model.FriendRequestListPagingResponse;
import com.chat.user_service.repository.FriendRequestRepository;
import com.chat.user_service.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

}
