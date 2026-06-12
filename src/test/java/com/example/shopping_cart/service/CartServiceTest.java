package com.example.shopping_cart.service;

import com.example.shopping_cart.dto.AddToCartRequestDto;
import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.exception.CartAlreadyExistsException;
import com.example.shopping_cart.exception.CartNotFoundException;
import com.example.shopping_cart.mapper.CartMapper;
import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.producer.CartEventProducer;
import com.example.shopping_cart.repository.CartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    CartRepository cartRepository;
    CartMapper cartMapper;
    CartService cartService;
    CartEventProducer cartEventProducer;

    @BeforeEach
    void setup() {
        this.cartMapper = Mappers.getMapper(CartMapper.class);
        this.cartRepository = Mockito.mock(CartRepository.class);
        this.cartEventProducer = Mockito.mock(CartEventProducer.class);
        this.cartService = new CartService(cartRepository,cartMapper,cartEventProducer);
    }
    @Nested
    class CreateCartTest {
        @Test
        void create_DataCorrect_ReturnsCartDto() {
            //given
            Long userId = 1L;
            Cart cart = new Cart(1L,1L,null,null);
            when(cartRepository.save(any())).thenReturn(cart);

            //when
            CartDto result = cartService.create(userId);

            //then
            Assertions.assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1L,result.userId())
            );
        }

        @Test
        void create_AlreadyExists_ThrowsCartAlreadyExistsException() {
            Long userId = 1L;
            when(cartRepository.existsByUserId(any())).thenReturn(true);

            CartAlreadyExistsException exception = assertThrows(CartAlreadyExistsException.class,
                    ()-> cartService.create(1L));

            Assertions.assertAll(
                    () -> assertEquals("Cart already exists for this user",exception.getMessage())
            );
        }
    }

    @Nested
    class AddItemToCartTests {

        @Test
        void addItemToCart_dataCorrect_ReturnsCartDto() {
            // Given
            AddToCartRequestDto request = new AddToCartRequestDto(1L, 100L, 2);
            Cart existingCart = new Cart(1L, 1L, LocalDateTime.now(), new ArrayList<>());

            when(cartRepository.getCartByUserId(request.userId())).thenReturn(Optional.of(existingCart));
            when(cartRepository.save(existingCart)).thenReturn(existingCart);

            // When
            CartDto result = cartService.addItemToCart(request);

            // Then
            assertNotNull(result);
            verify(cartRepository, times(1)).getCartByUserId(request.userId());
            verify(cartRepository, times(1)).save(existingCart);
        }

        @Test
        void addItemToCart_CartNotExists_CreateNewCartAndReturnsDto() {
            // Given
            AddToCartRequestDto request = new AddToCartRequestDto(1L, 100L, 2);
            when(cartRepository.getCartByUserId(request.userId())).thenReturn(Optional.empty());
            when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            CartDto result = cartService.addItemToCart(request);

            // Then
            assertNotNull(result);
            verify(cartRepository, times(1)).getCartByUserId(request.userId());
            verify(cartRepository, times(1)).save(any(Cart.class));
        }
    }

    @Nested
    class GetCartTests {

        @Test
        void shouldReturnCartWhenExists() {
            // Given
            Long userId = 1L;
            Cart cart = new Cart(1L, userId, LocalDateTime.now(), new ArrayList<>());
            when(cartRepository.getCartByUserId(userId)).thenReturn(Optional.of(cart));

            // When
            CartDto result = cartService.getCart(userId);

            // Then
            assertNotNull(result);
            assertEquals(userId, result.userId());
        }

        @Test
        void shouldThrowExceptionWhenCartNotFound() {
            // Given
            Long userId = 1L;
            when(cartRepository.getCartByUserId(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CartNotFoundException.class, () -> cartService.getCart(userId));
        }
    }

    @Nested
    class DeleteCartTests {

        @Test
        void shouldDeleteCartSuccessfully() {
            // Given
            Long cartId = 1L;
            Cart cart = new Cart(cartId, 1L, LocalDateTime.now(), new ArrayList<>());
            when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

            // When
            assertDoesNotThrow(() -> cartService.delete(cartId));

            // Then
            verify(cartRepository, times(1)).findById(cartId);
            verify(cartRepository, times(1)).delete(cart);
        }

        @Test
        void shouldThrowExceptionWhenDeletingNonExistentCart() {
            // Given
            Long cartId = 1L;
            when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CartNotFoundException.class, () -> cartService.delete(cartId));
            verify(cartRepository, never()).delete(any(Cart.class));
        }
    }


}
