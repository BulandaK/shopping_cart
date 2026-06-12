package com.example.shopping_cart.mapper;

import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.dto.CartItemResponseDto;
import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
    CartItemResponseDto toDto(CartItem cartItem);
}
