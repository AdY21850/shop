package com.example.sweet.data.dto;

import com.example.sweet.data.model.User;

public class LoginResponse {
    public boolean success;
    public String message;
    public String token;
    public User user;
}
