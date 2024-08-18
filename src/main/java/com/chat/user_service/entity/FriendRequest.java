package com.chat.user_service.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "friend_requests")
@Data
public class FriendRequest {

  public enum Status {
    PENDING, ACCEPTED, DENIED
  }

  @Id
  private UUID id;

  @Column("sender_id")
  private UUID senderId;
  @Column("recipient_id")
  private UUID recipientId;
  private Status status;

  @Transient
  private User sender;
  @Transient
  private User recipient;

  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;
  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;
}
