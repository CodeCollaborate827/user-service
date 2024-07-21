package com.chat.user_service.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "friendships")
@Data
public class FriendShip {

  @Id
  private UUID id;

  private UUID user1Id;
  private UUID user2Id;

  @CreatedDate
  private OffsetDateTime createdAt;
  @LastModifiedDate
  private OffsetDateTime updatedAt;
}
