package com.example.shopping_cart.repository;

import com.example.shopping_cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUserId(Long userId);
    Optional<Cart> getCartByUserId(Long userId);
}
