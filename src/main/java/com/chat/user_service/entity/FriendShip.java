package com.chat.user_service.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Table(name = "friendships")
@Data
public class FriendShip {

  @Id
  private Long id;

  private String user1Id;
  private String user2Id;

  @CreatedDate
  private OffsetDateTime createdAt;
  @LastModifiedDate
  private OffsetDateTime updatedAt;
}
