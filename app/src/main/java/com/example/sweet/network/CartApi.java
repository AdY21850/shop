package com.example.sweet.network;

import com.example.sweet.data.dto.CartResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartApi {

    // ✅ Fetch current user's cart
    @GET("/api/cart")
    Call<CartResponse> getCart();

    // ✅ Add sweet to cart (NO response body)
    @POST("/api/cart/add/{sweetId}")
    Call<Void> addToCart(
            @Path("sweetId") Long sweetId
    );

    // ✅ Update item quantity
    @PUT("/api/cart/update/{itemId}")
    Call<CartResponse> updateQuantity(
            @Path("itemId") Long itemId,
            @Query("quantity") int quantity
    );

    // ✅ Remove item from cart
    @DELETE("/api/cart/remove/{itemId}")
    Call<CartResponse> removeItem(
            @Path("itemId") Long itemId
    );

    // ✅ Clear entire cart (NO response body)
    @DELETE("/api/cart/clear")
    Call<Void> clearCart();
}
