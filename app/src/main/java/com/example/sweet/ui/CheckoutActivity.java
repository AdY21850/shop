package com.example.sweet.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sweet.R;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.OrderApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private TextView subtotalPrice, deliveryFee, discountAmount, totalPrice;
    private Button placeOrderButton;
    private ImageView backIcon;
    private TextView backText;

    private boolean orderPlaced = false; // 🔒 prevent double order

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        bindViews();
        setupBack();
        populateOrderSummary();
        setupPlaceOrder();
    }

    private void bindViews() {
        subtotalPrice = findViewById(R.id.subtotalPrice);
        deliveryFee = findViewById(R.id.deliveryFee);
        discountAmount = findViewById(R.id.discountAmount);
        totalPrice = findViewById(R.id.totalPrice);

        placeOrderButton = findViewById(R.id.placeOrderButton);
        backIcon = findViewById(R.id.backIcon);
        backText = findViewById(R.id.backText);
    }

    private void setupBack() {
        backIcon.setOnClickListener(v -> finish());
        backText.setOnClickListener(v -> finish());
    }

    private void populateOrderSummary() {
        double subtotal = getIntent().getDoubleExtra("subtotal", 0.0);
        double delivery = getIntent().getDoubleExtra("delivery_fee", 0.0);
        double discount = getIntent().getDoubleExtra("discount", 0.0);
        double total = getIntent().getDoubleExtra("total", 0.0);

        subtotalPrice.setText("₹" + subtotal);
        deliveryFee.setText("₹" + delivery);
        discountAmount.setText("-₹" + discount);
        totalPrice.setText("₹" + total);
    }

    private void setupPlaceOrder() {

        placeOrderButton.setOnClickListener(v -> {

            if (orderPlaced) return; // 🔒 safety

            orderPlaced = true;
            placeOrderButton.setEnabled(false);
            placeOrderButton.setText("Placing Order...");

            OrderApi api = ApiClient.getClient(this).create(OrderApi.class);

            api.placeOrder().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (!response.isSuccessful()) {
                        resetButton();
                        Toast.makeText(
                                CheckoutActivity.this,
                                "Failed to place order",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    Toast.makeText(
                            CheckoutActivity.this,
                            "🎉 Order placed successfully!",
                            Toast.LENGTH_LONG
                    ).show();

                    finish(); // or navigate to OrderSuccessActivity
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    resetButton();
                    Toast.makeText(
                            CheckoutActivity.this,
                            "Network error",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });
    }

    private void resetButton() {
        orderPlaced = false;
        placeOrderButton.setEnabled(true);
        placeOrderButton.setText("Place Order");
    }
}
