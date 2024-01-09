package com.securityVideoProject.security.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record OrderResponseDto(
        Integer id,
        Integer orderQuantity,
        String deliveryAddress,
        Integer userId,
        Integer productId,
        Date createdAt,
        Date updatedAt
) {
}
