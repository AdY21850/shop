package com.example.sweet.data.dto;

public class ReviewResponse {

    private long id;
    private int rating;
    private String comment;
    private String userName;
    private String createdAt;

    private String image;

    public String getImage() {
        return image;
    }



    // Required empty constructor for Retrofit
    public ReviewResponse() {
    }

    public long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getUserName() {
        return userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
