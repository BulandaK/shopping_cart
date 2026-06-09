package com.example.shopping_cart.exception;

import org.springframework.http.HttpStatus;

public class QuantityIsNotPositiveException extends CartException {
    public QuantityIsNotPositiveException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
