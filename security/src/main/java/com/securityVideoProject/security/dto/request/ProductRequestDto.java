package com.securityVideoProject.security.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public record ProductRequestDto(
        String productName,
        Integer productPrice,
        Integer quantity
) {
}
