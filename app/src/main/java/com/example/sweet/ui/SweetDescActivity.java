package com.example.sweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.sweet.R;
import com.example.sweet.data.dto.CartResponse;
import com.example.sweet.data.dto.ReviewResponse;
import com.example.sweet.data.model.Review;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.CartApi;
import com.example.sweet.network.ReviewApi;
import com.example.sweet.ui.adapter.ReviewAdapter;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SweetDescActivity extends AppCompatActivity {

    private static final String TAG = "SweetDescActivity";

    // =========================
    // UI
    // =========================
    private ImageView productImage, backButton;
    private TextView backText, productTitle, ratingValue;
    private RatingBar productRating;
    private TextView txtQuantity, totalPriceText, productDescription;
    private TextView btnPlus, btnMinus;
    private Button addToCartButton;
    private RecyclerView reviewsRecyclerView;



    // =========================
    // DATA
    // =========================
    private long sweetId;
    private String sweetName;
    private double sweetPrice;
    private String sweetImage;
    private boolean sweetActive;

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sweet_desc);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindViews();
        readIntentData();
        setupUI();
        setupClicks();
        setupReviewsRecycler();
        fetchReviews();
    }

    // =========================
    // BIND VIEWS
    // =========================
    private void bindViews() {

        productImage = findViewById(R.id.productImage);
        backButton = findViewById(R.id.backButton);
        backText = findViewById(R.id.backText);
        productTitle = findViewById(R.id.productTitle);
        productRating = findViewById(R.id.productRating);
        ratingValue = findViewById(R.id.ratingValue);
        txtQuantity = findViewById(R.id.txtQuantity);
        totalPriceText = findViewById(R.id.totalPriceText);
        productDescription = findViewById(R.id.productDescription);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        addToCartButton = findViewById(R.id.addToCartButton);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
    }

    // =========================
    // READ INTENT DATA
    // =========================
    private void readIntentData() {

        Intent intent = getIntent();

        sweetId = intent.getLongExtra("sweet_id", -1);
        sweetName = intent.getStringExtra("sweet_name");
        sweetPrice = intent.getDoubleExtra("sweet_price", 0);
        sweetImage = intent.getStringExtra("sweet_image");
        sweetActive = intent.getBooleanExtra("sweet_active", false);

        Log.e(TAG, "Received Sweet → id=" + sweetId
                + ", name=" + sweetName
                + ", price=" + sweetPrice
                + ", image=" + sweetImage
                + ", active=" + sweetActive);

        if (sweetId == -1) {
            Toast.makeText(this, "Invalid Sweet", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // =========================
    // SETUP UI
    // =========================
    private void setupUI() {

        productTitle.setText(sweetName != null ? sweetName : "Sweet");

        productDescription.setText(
                "Delicious handcrafted sweet made with premium ingredients."
        );

        productRating.setRating(4.5f);
        ratingValue.setText("4.5");

        txtQuantity.setText(String.valueOf(quantity));
        updateTotalPrice();

        if (sweetImage != null && !sweetImage.isEmpty()) {
            Glide.with(this)
                    .load(sweetImage)
                    .placeholder(R.drawable.cupcake1)
                    .error(R.drawable.cupcake1)
                    .into(productImage);
        } else {
            productImage.setImageResource(R.drawable.cupcake1);
        }
    }

    // =========================
    // REVIEWS SETUP
    // =========================
//    private static final String TAG = "SweetDescReviews";

    private void setupReviewsRecycler() {

        Log.e(TAG, "🚀 setupReviewsRecycler() CALLED");

        if (reviewsRecyclerView == null) {
            Log.e(TAG, "❌ reviewsRecyclerView is NULL");
            return;
        }

        Log.e(TAG, "✅ reviewsRecyclerView FOUND");

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                );

        reviewsRecyclerView.setLayoutManager(layoutManager);
        Log.e(TAG, "✅ LayoutManager SET → VERTICAL");

        reviewsRecyclerView.setNestedScrollingEnabled(false);
        Log.e(TAG, "✅ Nested scrolling DISABLED");

        reviewsRecyclerView.setHasFixedSize(false);
        Log.e(TAG, "✅ hasFixedSize = false");

        Log.e(TAG, "🎯 Reviews Recycler setup COMPLETE");
    }



    private void fetchReviews() {

        Log.e(TAG, "Fetching reviews for sweetId = " + sweetId);

        ReviewApi api = ApiClient.getClient(this).create(ReviewApi.class);

        api.getReviewsForSweet(sweetId)
                .enqueue(new Callback<List<ReviewResponse>>() {

                    @Override
                    public void onResponse(
                            Call<List<ReviewResponse>> call,
                            Response<List<ReviewResponse>> response
                    ) {

                        Log.e(TAG, "Reviews response code = " + response.code());
                        Log.e(
                                "RAW_REVIEW_JSON",
                                new Gson().toJson(response.body())
                        );

                        if (!response.isSuccessful() || response.body() == null) {
                            Log.e(TAG, "❌ Reviews API failed");
                            return;
                        }

                        List<ReviewResponse> reviews = response.body();
                        Log.e(TAG, "✅ Reviews received = " + reviews.size());
                        Log.e(
                                "RAW_REVIEW_JSON",
                                new Gson().toJson(response.body())
                        );

                        reviewsRecyclerView.setAdapter(
                                new ReviewAdapter(reviews)
                        );
                    }

                    @Override
                    public void onFailure(
                            Call<List<ReviewResponse>> call,
                            Throwable t
                    ) {
                        Log.e(TAG, "❌ Reviews API error", t);
                    }
                });
    }


    // =========================
    // CLICKS
    // =========================
    private void setupClicks() {

        backButton.setOnClickListener(v -> finish());
        backText.setOnClickListener(v -> finish());

        btnPlus.setOnClickListener(v -> {
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        addToCartButton.setOnClickListener(v -> {

            Log.e(TAG, "🛒 Add to Cart clicked → sweetId=" + sweetId);

            CartApi api = ApiClient.getClient(this).create(CartApi.class);

            api.addToCart(sweetId).enqueue(new Callback<Void>() {

                @Override
                public void onResponse(
                        Call<Void> call,
                        Response<Void> response
                ) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(
                                SweetDescActivity.this,
                                "Failed to add to cart",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    // ✅ Successfully added to cart
                    startActivity(
                            new Intent(
                                    SweetDescActivity.this,
                                    CartActivity.class
                            )
                    );
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(
                            SweetDescActivity.this,
                            "Network error",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.e("CART_API", "Error", t);
                }
            });

        });

    }

    // =========================
    // TOTAL PRICE
    // =========================
    private void updateTotalPrice() {
        double total = sweetPrice * quantity;
        totalPriceText.setText("₹" + total);
    }
}
