package com.example.sweet.data.dto;

public class CartItemResponse {
    private Long id;
    private int quantity;
    private SweetResponse sweet;

    public Long getId() { return id; }
    public int getQuantity() { return quantity; }
    public SweetResponse getSweet() { return sweet; }
}
