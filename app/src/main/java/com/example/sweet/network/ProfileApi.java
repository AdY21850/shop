package com.example.sweet.network;

import com.example.sweet.data.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ProfileApi {

    // ==========================
    // GET LOGGED-IN USER PROFILE
    // ==========================
    @Headers("Content-Type: application/json")
    @GET("/api/profile")
    Call<User> getProfile();

    // ==========================
    // UPDATE PROFILE (NAME / PHONE / IMAGE)
    // ==========================
    @Headers("Content-Type: application/json")
    @PUT("/api/profile")
    Call<User> updateProfile(
            @Body Map<String, String> payload
    );

    // ==========================
    // UPDATE PROFILE IMAGE ONLY
    // ==========================
    @PUT("/api/profile/image")
    Call<User> updateProfileImage(
            @Query("imageUrl") String imageUrl
    );
}
