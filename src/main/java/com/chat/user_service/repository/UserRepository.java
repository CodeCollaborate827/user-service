package com.chat.user_service.repository;

import com.chat.user_service.dto.UserTotalSearchDTO;
import com.chat.user_service.entity.User;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, UUID> {

  @Query(
      "INSERT INTO users (id, username, display_name, avatar_url, email, date_of_birth, gender, created_at, updated_at) "
          + "VALUES (:id, :username, :displayName, :avatarUrl, :email, :dateOfBirth, :gender, :createdAt, :updatedAt)")
  Mono<Void> saveUserWithId(
      UUID id,
      String username,
      String displayName,
      String avatarUrl,
      String email,
      LocalDate dateOfBirth,
      String gender,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt);

  default Mono<Void> saveUserWithId(User user) {
    return saveUserWithId(
        user.getId(),
        user.getUsername(),
        user.getDisplayName(),
        user.getAvatarUrl(),
        user.getEmail(),
        user.getDateOfBirth(),
        user.getGender().name(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }

  @Query(
      """
        WITH search_results AS (
          SELECT id as user_id, username, display_name, avatar_url, created_at
          FROM users
          WHERE CASE
            WHEN :keyword LIKE '#%' THEN username = :exactUsername
            ELSE LOWER(display_name) LIKE :likePattern
          END
        )
        SELECT user_id, username, display_name, avatar_url, count(*) OVER() as total_count
        FROM search_results
        ORDER BY created_at DESC
        LIMIT :pageSize OFFSET :offset
    """)
  Flux<UserTotalSearchDTO> searchUsers(
      String keyword, String exactUsername, String likePattern, int pageSize, long offset);
}
