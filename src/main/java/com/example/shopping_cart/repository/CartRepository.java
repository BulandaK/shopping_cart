package com.example.shopping_cart.repository;

import com.example.shopping_cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUserId(Long userId);
}
