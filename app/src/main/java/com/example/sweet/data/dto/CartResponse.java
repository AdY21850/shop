package com.example.sweet.data.dto;

import java.util.List;

public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;

    public Long getId() { return id; }
    public List<CartItemResponse> getItems() { return items; }
}
