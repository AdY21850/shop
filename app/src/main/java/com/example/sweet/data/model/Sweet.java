package com.example.sweet.data.model;

import com.google.gson.annotations.SerializedName;

public class Sweet {

    private Long id;
    private String name;
    private double price;
    private String category;
    // ✅ FIXED: backend uses imageUrl, NOT image_url
    @SerializedName("imageUrl")
    private String imageUrl;

    private boolean active;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getCategory() {
        return category;
    }


    public boolean isActive() {
        return active;
    }
}
