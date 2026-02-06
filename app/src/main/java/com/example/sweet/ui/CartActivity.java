package com.example.sweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweet.R;
import com.example.sweet.data.dto.CartItemResponse;
import com.example.sweet.data.dto.CartResponse;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.CartApi;
import com.example.sweet.ui.adapter.CartAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity
        implements CartAdapter.CartUpdateListener {

    private RecyclerView recyclerView;
    private Button checkoutButton;

    private TextView subtotalPrice, deliveryFee, discountAmount, totalPrice;

    private double subtotal = 0.0;
    private double delivery = 2.0;   // static for now
    private double discount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartRecyclerView);
        checkoutButton = findViewById(R.id.checkoutButton);

        subtotalPrice = findViewById(R.id.subtotalPrice);
        deliveryFee = findViewById(R.id.deliveryFee);
        discountAmount = findViewById(R.id.discountAmount);
        totalPrice = findViewById(R.id.totalPrice);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCart();

        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("subtotal", subtotal);
            intent.putExtra("delivery_fee", delivery);
            intent.putExtra("discount", discount);
            intent.putExtra("total", subtotal + delivery - discount);
            startActivity(intent);
        });
    }

    private void loadCart() {

        CartApi api = ApiClient.getClient(this).create(CartApi.class);

        api.getCart().enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(
                    Call<CartResponse> call,
                    Response<CartResponse> response
            ) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(
                            CartActivity.this,
                            "Cart is empty",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                List<CartItemResponse> items = response.body().getItems();

                recyclerView.setAdapter(
                        new CartAdapter(items, api, CartActivity.this)
                );

                calculateTotals(items);
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(
                        CartActivity.this,
                        "Failed to load cart",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void calculateTotals(List<CartItemResponse> items) {
        subtotal = 0.0;

        for (CartItemResponse item : items) {
            subtotal += item.getSweet().getPrice() * item.getQuantity();
        }

        subtotalPrice.setText("₹" + subtotal);
        deliveryFee.setText("₹" + delivery);
        discountAmount.setText("-₹" + discount);
        totalPrice.setText("₹" + (subtotal + delivery - discount));
    }

    @Override
    public void onCartUpdated() {
        loadCart(); // 🔁 refresh everything
    }
}
