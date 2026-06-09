package com.example.shopping_cart.model;

import com.example.shopping_cart.exception.QuantityIsNotPositiveException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    public void addProduct(Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new QuantityIsNotPositiveException("Quantity have to be positive");
        }

        Optional<CartItem> existingItem = this.items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            int newQuantity = existingItem.get().getQuantity() + quantity;
            existingItem.get().setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem(null, this, productId, quantity);
            this.items.add(newItem);
        }
    }
}
