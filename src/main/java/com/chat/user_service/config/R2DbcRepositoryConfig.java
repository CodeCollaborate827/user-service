package com.chat.user_service.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.chat.user_service.repository")
public class R2DbcRepositoryConfig {
}
