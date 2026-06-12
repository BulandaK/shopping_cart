package com.example.shopping_cart.service;

import com.example.shopping_cart.dto.AddToCartRequestDto;
import com.example.shopping_cart.dto.CartCheckoutEvent;
import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.exception.CartAlreadyExistsException;
import com.example.shopping_cart.exception.CartNotFoundException;
import com.example.shopping_cart.mapper.CartMapper;
import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.producer.CartEventProducer;
import com.example.shopping_cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartEventProducer cartEventProducer;

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

    @Transactional
    public void checkout(Long userId) {
        Cart cart = cartRepository.getCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        List<CartCheckoutEvent.CartItemEventDto> eventItems = cart.getItems().stream()
                .map(item -> new CartCheckoutEvent.CartItemEventDto(
                        item.getProductId(),
                        item.getQuantity(),
                        new BigDecimal("99.99")
                )).toList();

        CartCheckoutEvent event = new CartCheckoutEvent(userId, eventItems);

        cartEventProducer.sendCheckoutEvent(event);
    }
}
