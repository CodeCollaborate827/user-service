package com.chat.user_service.service;

import com.chat.user_service.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendshipService {

  @Autowired
  private FriendshipRepository friendshipRepository;

  @Autowired
  private UserService userService;


//  public Mono<ResponseEntity<>> getUserFriends(String userId) {
//    return friendshipRepository.findAllByUser1IdOrOrUser2Id(userId)
//            .map(friendShip -> {
//              // friendship contains the current user id and the other user id
//              // only get the other user id from the friendship
//              if (friendShip.getUser1Id().contains("test")) {
//                return friendShip.getUser2Id();
//              } else {
//                return friendShip.getUser1Id();
//              }
//            })
//            .flatMap(userService::getUser)
//            .collectList()
//            .map(list -> {
//
//            })
//  }
}
