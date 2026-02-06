package com.example.sweet.network;

import com.example.sweet.data.dto.ReviewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReviewApi {

    @GET("/api/reviews/{sweetId}")
    Call<List<ReviewResponse>> getReviewsForSweet(
            @Path("sweetId") long sweetId
    );
}
