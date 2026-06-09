package com.example.shopping_cart.dto;

public record CartItemResponseDto(
        Long productId,
        Integer quantity
) {
}
