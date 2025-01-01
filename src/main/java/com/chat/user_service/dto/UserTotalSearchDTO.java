package com.chat.user_service.dto;

import lombok.Data;

@Data
public class UserTotalSearchDTO {
    private String userId;

    private String username;

    private String displayName;

    private String avatarUrl;

    private int totalCount;
}
