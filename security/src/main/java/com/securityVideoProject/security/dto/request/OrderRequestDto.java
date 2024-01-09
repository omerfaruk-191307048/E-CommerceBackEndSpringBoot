package com.securityVideoProject.security.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public record OrderRequestDto(
        Integer orderQuantity,
        String deliveryAddress,
        Integer userId,
        Integer productId
) {
}
