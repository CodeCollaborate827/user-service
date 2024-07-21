package com.chat.user_service.repository;

import com.chat.user_service.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends R2dbcRepository<User, UUID> {
}
