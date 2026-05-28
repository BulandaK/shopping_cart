package com.example.shopping_cart.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CartException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CartException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
