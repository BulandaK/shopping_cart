package com.example.shopping_cart.controller;

import com.example.shopping_cart.dto.AddToCartRequestDto;
import com.example.shopping_cart.dto.CartDto;
import com.example.shopping_cart.dto.CreateCartCommand;
import com.example.shopping_cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartService cartService;

    @Test
    @DisplayName("POST /api/v1/cart -> Should return 200 OK and CartDto when cart is created")
    void shouldCreateCart() throws Exception {
        // Given
        CreateCartCommand command = new CreateCartCommand(1L);
        CartDto expectedDto = new CartDto(1L, 1L, LocalDateTime.now(), new ArrayList<>());

        when(cartService.create(command.userId())).thenReturn(expectedDto);

        // When & Then
        mockMvc.perform(post("/api/v1/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L));

        verify(cartService, times(1)).create(command.userId());
    }

    @Test
    @DisplayName("POST /api/v1/cart/item -> Should return 200 OK and updated CartDto when item is added")
    void shouldAddItemToCart() throws Exception {
        // Given
        AddToCartRequestDto request = new AddToCartRequestDto(1L, 100L, 2);
        CartDto expectedDto = new CartDto(1L, 1L, LocalDateTime.now(), new ArrayList<>());

        when(cartService.addItemToCart(any(AddToCartRequestDto.class))).thenReturn(expectedDto);

        // When & Then
        mockMvc.perform(post("/api/v1/cart/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(cartService, times(1)).addItemToCart(request);
    }

    @Test
    @DisplayName("GET /api/v1/cart/user/{userId} -> Should return 200 OK and CartDto when found")
    void shouldGetCartByUserId() throws Exception {
        // Given
        Long userId = 1L;
        CartDto expectedDto = new CartDto(1L, userId, LocalDateTime.now(), new ArrayList<>());

        when(cartService.getCart(userId)).thenReturn(expectedDto);

        // When & Then
        mockMvc.perform(get("/api/v1/cart/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));

        verify(cartService, times(1)).getCart(userId);
    }

    @Test
    @DisplayName("DELETE /api/v1/cart/{id} -> Should return 200 OK when cart is deleted")
    void shouldDeleteCart() throws Exception {
        // Given
        Long cartId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/v1/cart/{id}", cartId))
                .andExpect(status().isOk());

        verify(cartService, times(1)).delete(cartId);
    }
}