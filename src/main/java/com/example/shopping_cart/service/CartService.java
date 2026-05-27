package com.example.shopping_cart.service;

import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.mapper.CartMapper;
import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper mapper;

    @Transactional
    public CartDto create(Long userId) {
        if(cartRepository.existsByUserId(userId)) {
            System.out.println("user already has cart");
        }
        Cart cart = new Cart(null,userId, LocalDateTime.now(),null);
        Cart saved = cartRepository.save(cart);
        return mapper.toDto(saved);
    }
}
