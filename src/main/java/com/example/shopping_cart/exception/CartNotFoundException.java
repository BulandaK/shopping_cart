package com.example.shopping_cart.exception;


import org.springframework.http.HttpStatus;

public class CartNotFoundException extends CartException {
    public CartNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
