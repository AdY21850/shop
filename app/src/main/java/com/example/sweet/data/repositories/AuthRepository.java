//package com.example.sweet.data.repositories;
//
//import com.example.sweet.data.dto.GoogleTokenRequest;
//import com.example.sweet.data.dto.LoginRequest;
//import com.example.sweet.data.dto.LoginResponse;
//import com.example.sweet.data.dto.RegisterRequest;
//import com.example.sweet.network.ApiClient;
//import com.example.sweet.network.AuthApi;
//
//import retrofit2.Call;
//
//public class AuthRepository {
//
//    private final AuthApi authApi;
//
//    public AuthRepository() {
//        authApi = ApiClient
//                .getClient(this)
//                .create(AuthApi.class);
//    }
//
//    // ==========================
//    // EMAIL LOGIN
//    // ==========================
//    public Call<LoginResponse> login(LoginRequest request) {
//        return authApi.login(request);
//    }
//
//    // ==========================
//    // EMAIL REGISTER
//    // ==========================
//    public Call<LoginResponse> register(RegisterRequest request) {
//        return authApi.register(request);
//    }
//
//    // ==========================
//    // GOOGLE LOGIN
//    // ==========================
//    public Call<LoginResponse> googleLogin(String token) {
//        return authApi.googleLogin(new GoogleTokenRequest(token));
//    }
//
//    // ==========================
//    // GOOGLE REGISTER
//    // ==========================
//    public Call<LoginResponse> googleRegister(String token) {
//        return authApi.googleRegister(new GoogleTokenRequest(token));
//    }
//}
