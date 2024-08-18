package com.chat.user_service.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "friendships")
@Data
public class FriendShip {

  @Id
  private UUID id;

  @Column("user_1_id")
  private UUID user1Id;

  @Column("user_2_id")
  private UUID user2Id;

  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;
  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;
}
