package com.example.sweet.network;

import com.example.sweet.data.model.Sweet;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SweetApi {

    // GET /api/sweets/popular?limit=10
    @GET("api/sweets/popular")
    Call<List<Sweet>> getPopularSweets(
            @Query("limit") int limit
    );
    @GET("api/sweets/by-category")
    Call<List<Sweet>> getSweetsByCategory(
            @Query("category") String category
    );

}
