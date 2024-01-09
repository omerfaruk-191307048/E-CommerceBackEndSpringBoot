package com.securityVideoProject.security.dto.response;

import java.util.Date;

public record ProductResponseDto (
        Integer id,
        String productName,
        Integer productPrice,
        Integer quantity,
        Date createdAt,
        Date updatedAt
) {
}

