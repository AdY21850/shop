package com.example.sweet.data.model;

import com.example.sweet.data.model.User;

public class Review {

    private Long id;
    private int rating;
    private String comment;
    private User user;

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }
}
