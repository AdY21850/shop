package com.example.sweet.network;

import com.example.sweet.data.model.HeroSection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface HeroApi {

    // GET https://sweet-e8ar.onrender.com/api/hero/active
    @Headers("Content-Type: application/json")
    @GET("api/hero/active")
    Call<List<HeroSection>> getActiveHeroes();
}
