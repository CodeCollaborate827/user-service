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

@Table(name = "user_address")
@Data
public class UserAddress {

  @Id
  private UUID id;

  @Column("user_id")
  private UUID userId;
  private String country;
  private String province;
  private String city;
  private String district;
  private String ward;

  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;
  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;
}
