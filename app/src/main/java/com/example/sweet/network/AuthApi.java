package com.example.sweet.network;

import com.example.sweet.data.dto.GoogleTokenRequest;
import com.example.sweet.data.dto.LoginRequest;
import com.example.sweet.data.dto.LoginResponse;
import com.example.sweet.data.dto.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthApi {

    // ==========================
    // EMAIL LOGIN
    // ==========================
    @Headers("Content-Type: application/json")
    @POST("/api/auth/login")
    Call<LoginResponse> login(
            @Body LoginRequest request
    );

    // ==========================
    // EMAIL REGISTER
    // ==========================
    @Headers("Content-Type: application/json")
    @POST("/api/auth/register")
    Call<LoginResponse> register(
            @Body RegisterRequest request
    );

    // ==========================
    // GOOGLE LOGIN
    // ==========================
    @Headers("Content-Type: application/json")
    @POST("/api/auth/google-login")
    Call<LoginResponse> googleLogin(
            @Body GoogleTokenRequest request
    );

    // ==========================
    // GOOGLE REGISTER
    // ==========================
    @Headers("Content-Type: application/json")
    @POST("/api/auth/google-register")
    Call<LoginResponse> googleRegister(
            @Body GoogleTokenRequest request
    );
}
