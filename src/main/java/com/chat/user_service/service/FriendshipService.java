package com.chat.user_service.service;

import com.chat.user_service.repository.FriendshipRepository;
import com.chat.user_service.server.model.GetUserFriends200Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FriendshipService {

  @Autowired
  private FriendshipRepository friendshipRepository;

  @Autowired
  private UserService userService;


  public Mono<ResponseEntity<GetUserFriends200Response>> getUserFriends(String userId) {
    return friendshipRepository.findAllByUser1IdOrOrUser2Id(userId)
            .map(friendShip -> {
              // friendship contains the current user id and the other user id
              // only get the other user id from the friendship
              if (friendShip.getUser1Id().contains("test")) {
                return friendShip.getUser2Id();
              } else {
                return friendShip.getUser1Id();
              }
            })
            .flatMap(userService::getUser)
            .collectList()
            .map(list -> {

            })
  }
}
