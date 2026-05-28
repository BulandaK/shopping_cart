package com.example.shopping_cart.service;

import com.example.shopping_cart.dto.AddToCartRequestDto;
import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.mapper.CartMapper;
import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartDto create(Long userId) {
        if(cartRepository.existsByUserId(userId)) {
            System.out.println("user already has cart");
            return null;
        }
        Cart cart = new Cart(null,userId, LocalDateTime.now(),null);
        Cart saved = cartRepository.save(cart);
        return cartMapper.toDto(saved);
    }

    @Transactional
    public CartDto addItemToCart(AddToCartRequestDto request) {
        Cart cart = cartRepository.getCartByUserId(request.userId())
                .orElseGet(() -> createNewCartForUser(request.userId()));
        cart.addProduct(request.productId(), request.quantity());
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    private Cart createNewCartForUser(Long userId) {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setItems(new ArrayList<>());
        return newCart;
    }

    public CartDto getCart(Long userId) {
        Cart cart = cartRepository.getCartByUserId(userId).orElseThrow();
        return cartMapper.toDto(cart);
    }
}
