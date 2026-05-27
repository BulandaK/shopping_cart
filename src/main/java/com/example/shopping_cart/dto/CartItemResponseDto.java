package com.example.shopping_cart.dto;

public record CartItemResponseDto(
        Long itemId,
        Long productId,
        Integer quantity
) {
}
