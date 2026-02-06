package com.example.sweet.network;

import retrofit2.Call;
import retrofit2.http.POST;

public interface OrderApi {

    // ✅ Place order from cart (no response body)
    @POST("/api/orders/place")
    Call<Void> placeOrder();
}
