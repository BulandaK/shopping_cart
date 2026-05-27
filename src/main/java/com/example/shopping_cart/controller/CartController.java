package com.example.shopping_cart.controller;

import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.dto.CreateCartCommand;
import com.example.shopping_cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public CartDto create(@RequestBody CreateCartCommand command) {
        return cartService.create(command.userId());
    }

}
