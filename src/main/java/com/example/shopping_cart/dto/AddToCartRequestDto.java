package com.example.shopping_cart.dto;

public record AddToCartRequestDto(
        Long userId,
        Long productId,
        Integer quantity
) {
}
