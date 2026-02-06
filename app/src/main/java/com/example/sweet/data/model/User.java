package com.example.sweet.data.model;

public class User {

    private Long id;
    private String email;
    private String username;
    private String fullName;
    private String phone;
    private String profileImageUrl;
    private String role;

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getRole() {
        return role;
    }
}
