package com.chat.user_service.entity;

import com.chat.user_service.event.UserRegistrationEvent;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "users")
@Data
public class User {

  @Id
  private UUID id;
  private String username;
  @Column("display_name")
  private String displayName;
  @Column("avatar_url")
  private String avatarUrl;
  private String email;

  @Column("date_of_birth")
  private LocalDate dateOfBirth;
  private Gender gender;

  @Transient
  private UserAddress address;


  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;
  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;

  public enum Gender {
    male,
    female,
    other
  }
}
