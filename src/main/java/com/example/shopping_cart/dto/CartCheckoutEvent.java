package com.example.shopping_cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartCheckoutEvent(
        Long userId,
        List<CartItemEventDto> items
) {
    public record CartItemEventDto(
            Long productId,
            Integer quantity,
            BigDecimal price
    ){}
}
