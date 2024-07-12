package com.chat.user_service.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Table(name = "friend_requests")
@Data
public class FriendRequest {

  public enum Status {
    PENDING, ACCEPTED, DENIED
  }

  @Id
  private Long id;

  private String senderId;
  private String recipientId;
  private Status status;

  @Transient
  private User sender;
  @Transient
  private User recipient;

  @CreatedDate
  private OffsetDateTime createdAt;
  @LastModifiedDate
  private OffsetDateTime updatedAt;
}
