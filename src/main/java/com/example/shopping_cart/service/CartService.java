package com.example.shopping_cart.service;

import com.example.shopping_cart.dto.AddToCartRequestDto;
import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.exception.CartAlreadyExistsException;
import com.example.shopping_cart.exception.CartNotFoundException;
import com.example.shopping_cart.mapper.CartMapper;
import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartDto create(Long userId) {
        if (cartRepository.existsByUserId(userId)) {
            throw new CartAlreadyExistsException("Cart already exists for this user");
        }
        Cart cart = new Cart(null, userId, LocalDateTime.now(), new ArrayList<>());
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

    public CartDto getCart(Long userId) {
        Cart cart = cartRepository.getCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        return cartMapper.toDto(cart);
    }

    private Cart createNewCartForUser(Long userId) {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setItems(new ArrayList<>());
        newCart.setCreatedAt(LocalDateTime.now());
        return newCart;
    }

    @Transactional
    public void delete(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart with id: " + id + " not found"));
        cartRepository.delete(cart);
    }
}
