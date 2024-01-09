package com.securityVideoProject.security.dto.response;

import java.util.Date;

public record UserResponseDto (
        Integer id,
        String firstname,
        String lastname,
        String email,
        String password,
        Date createdAt,
        Date updatedAt
) {
}
