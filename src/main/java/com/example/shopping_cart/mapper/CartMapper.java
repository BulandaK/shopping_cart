package com.example.shopping_cart.mapper;

import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(Cart cart);

}
