package com.example.shopping_cart.exception;

import org.springframework.http.HttpStatus;

public class CartAlreadyExistsException extends CartException {
    public CartAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
