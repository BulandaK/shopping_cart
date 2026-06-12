package com.example.shopping_cart.dto;

public record CartItemResponseDto(
        Long id,
        Long productId,
        Integer quantity
) {
}
