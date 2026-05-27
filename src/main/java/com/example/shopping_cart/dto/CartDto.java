package com.example.shopping_cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CartDto(
        Long id,
        Long userId,
        LocalDateTime createdAt,
        List<CartItemResponseDto> items
) {
}
