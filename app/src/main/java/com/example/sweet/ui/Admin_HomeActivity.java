package com.example.sweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.sweet.ui.Categories;
import com.example.sweet.R;
import com.example.sweet.data.model.Category;
import com.example.sweet.data.model.HeroSection;
import com.example.sweet.data.model.Sweet;
import com.example.sweet.data.model.User;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.CategoriesApi;
import com.example.sweet.network.HeroApi;
import com.example.sweet.network.ProfileApi;
import com.example.sweet.network.SweetApi;
import com.example.sweet.ui.adapter.CategoryAdapter;
import com.example.sweet.ui.adapter.OfferSliderAdapter;
import com.example.sweet.ui.adapter.PopularSweetAdapter;
import com.example.sweet.utils.SessionManager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private ImageView profileImage, iconSettings, iconProfile;
    private EditText searchInput;
    private RecyclerView categoriesRecycler, popularRecycler;
    private ViewPager2 offersViewPager;
    private TextView greetingText, seeAllBtn;
    private LinearLayout navHome, navOrders, navCart, navProfile;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);

        bindViews();
        setupRecyclerViews();
        setupClicks();

        // BACKEND CALLS
        loadUserProfile();
        loadHeroSlider();
        loadCategories();

        Log.d(TAG, "HomeActivity CREATED");
        loadPopularSweets();

        // ❌ REMOVE loadDummyData();
    }

    // ===============================
    // UI BINDING
    // ===============================
    private void bindViews() {
        profileImage = findViewById(R.id.profileImage);
        iconSettings = findViewById(R.id.iconSettings);
        iconProfile = findViewById(R.id.iconProfile);
        searchInput = findViewById(R.id.searchInput);
        categoriesRecycler = findViewById(R.id.categoriesRecycler);
        popularRecycler = findViewById(R.id.popularRecycler);
        offersViewPager = findViewById(R.id.offersViewPager);
        greetingText = findViewById(R.id.greetingText);
        seeAllBtn = findViewById(R.id.seeAllBtn);
        navHome = findViewById(R.id.navHome);
        navOrders = findViewById(R.id.navOrders);
        navCart = findViewById(R.id.navCart);
        navProfile = findViewById(R.id.navProfile);
    }

    private void setupRecyclerViews() {
        categoriesRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        popularRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
    }

    // ===============================
    // CLICKS
    // ===============================
    private void setupClicks() {

        profileImage.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        iconProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        iconSettings.setOnClickListener(v ->
                Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show()
        );

        searchInput.setFocusable(false);
        searchInput.setOnClickListener(v ->
                startActivity(new Intent(this, Categories.class))
        );

        seeAllBtn.setOnClickListener(v ->
                startActivity(new Intent(this, SweetDescActivity.class))
        );

        navHome.setOnClickListener(v -> recreate());
        navOrders.setOnClickListener(v -> startActivity(new Intent(this, Categories.class)));
        navCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    // ===============================
    // PROFILE API
    // ===============================
    private void loadUserProfile() {

        Log.d(TAG, "Calling /api/profile");

        ProfileApi api = ApiClient.getClient(this).create(ProfileApi.class);
        api.getProfile().enqueue(new retrofit2.Callback<User>() {

            @Override
            public void onResponse(
                    retrofit2.Call<User> call,
                    retrofit2.Response<User> response) {

                Log.d(TAG, "Profile response code: " + response.code());

                if (!response.isSuccessful() || response.body() == null) {
                    Log.e(TAG, "Profile failed or empty");
                    greetingText.setText("Welcome 👋");
                    return;
                }

                User user = response.body();
                Log.d(TAG, "Profile User: " + user.getEmail());

                greetingText.setText("Welcome 👋, " + user.getFullName());

                if (user.getProfileImageUrl() != null) {
                    Log.d(TAG, "Profile Image URL: " + user.getProfileImageUrl());

                    Glide.with(Admin_HomeActivity.this)
                            .load(user.getProfileImageUrl())
                            .placeholder(R.drawable.profile1)
                            .into(profileImage);
                }
            }

            @Override
            public void onFailure(
                    retrofit2.Call<User> call,
                    Throwable t) {

                Log.e(TAG, "Profile API error", t);
                greetingText.setText("Welcome 👋");
            }
        });
    }

    // ===============================
    // HERO API
    // ===============================
    private void loadHeroSlider() {

        Log.d(TAG, "Calling /api/hero/active");

        HeroApi heroApi = ApiClient.getClient(this).create(HeroApi.class);

        heroApi.getActiveHeroes().enqueue(new retrofit2.Callback<List<HeroSection>>() {

            @Override
            public void onResponse(
                    retrofit2.Call<List<HeroSection>> call,
                    retrofit2.Response<List<HeroSection>> response) {

                Log.d(TAG, "Hero response code: " + response.code());

                if (!response.isSuccessful()
                        || response.body() == null
                        || response.body().isEmpty()) {

                    Log.e(TAG, "No active heroes found");
                    return;
                }

                // Collect image URLs for slider
                List<String> imageUrls = new ArrayList<>();

                for (HeroSection hero : response.body()) {
                    if (hero.getImageUrl() != null && !hero.getImageUrl().isEmpty()) {
                        imageUrls.add(hero.getImageUrl());
                    }
                }

                if (imageUrls.isEmpty()) {
                    Log.e(TAG, "Hero images list is empty");
                    return;
                }

                OfferSliderAdapter adapter = new OfferSliderAdapter(response.body());
                offersViewPager.setAdapter(adapter);

                DotsIndicator dots = findViewById(R.id.offersDotsIndicator);
                dots.attachTo(offersViewPager);
            }

            @Override
            public void onFailure(
                    retrofit2.Call<List<HeroSection>> call,
                    Throwable t) {

                Log.e(TAG, "Hero API error", t);
            }
        });
    }

    // ===============================
    // CATEGORIES API
    // ===============================
    private void loadCategories() {

        Log.d(TAG, "Calling /api/categories");

        CategoriesApi api = ApiClient.getClient(this).create(CategoriesApi.class);
        api.getAllCategories().enqueue(new retrofit2.Callback<List<Category>>() {

            @Override
            public void onResponse(
                    retrofit2.Call<List<Category>> call,
                    retrofit2.Response<List<Category>> response) {

                Log.d(TAG, "Categories response code: " + response.code());

                if (!response.isSuccessful() || response.body() == null) {
                    Log.e(TAG, "Categories empty");
                    return;
                }

                List<Category> categories = response.body();
                Log.d(TAG, "Categories count: " + categories.size());

                categoriesRecycler.setAdapter(
                        new CategoryAdapter(
                                categories.stream().map(Category::getName).toList()
                        )
                );
            }

            @Override
            public void onFailure(
                    retrofit2.Call<List<Category>> call,
                    Throwable t) {

                Log.e(TAG, "Categories API error", t);
            }
        });
    }

    // ===============================
    // SAFE DUMMY DATA
    // ===============================

//    private static final String TAG = "PopularSweets";

//    private static final String TAG = "HomeActivity";

    private void loadPopularSweets() {

        SweetApi sweetApi =
                ApiClient.getClient(this).create(SweetApi.class);

        Log.d(TAG, "Calling getPopularSweets API...");

        sweetApi.getPopularSweets(10)
                .enqueue(new Callback<List<Sweet>>() {

                    @Override
                    public void onResponse(
                            Call<List<Sweet>> call,
                            Response<List<Sweet>> response
                    ) {

                        Log.d(TAG, "Response code: " + response.code());

                        if (!response.isSuccessful()) {
                            Log.e(TAG, "API failed: " + response.errorBody());
                            return;
                        }

                        if (response.body() == null) {
                            Log.e(TAG, "Response body is NULL");
                            return;
                        }

                        if (response.body().isEmpty()) {
                            Log.w(TAG, "Popular sweets list is EMPTY");
                            return;
                        }

                        // 🔥 LOG EACH SWEET
                        for (Sweet sweet : response.body()) {
                            Log.d(TAG,
                                    "Sweet → id=" + sweet.getId()
                                            + ", name=" + sweet.getName()
                                            + ", price=" + sweet.getPrice()
                                            + ", image=" + sweet.getImageUrl()
                            );
                        }

                        PopularSweetAdapter adapter =
                                new PopularSweetAdapter(response.body());

                        popularRecycler.setLayoutManager(
                                new GridLayoutManager(
                                        Admin_HomeActivity.this,
                                        2, // 👈 TWO ITEMS SIDE BY SIDE
                                        RecyclerView.VERTICAL, // 👈 SCROLL TOP → BOTTOM
                                        false
                                )
                        );


                        popularRecycler.setAdapter(adapter);

                        Log.d(TAG, "Popular sweets adapter attached successfully");
                    }

                    @Override
                    public void onFailure(
                            Call<List<Sweet>> call,
                            Throwable t
                    ) {
                        Log.e(TAG, "API call failed", t);
                    }
                });
    }




//    private void loadDummyData() {
//
//        popularRecycler.setAdapter(
//                new PopularSweetAdapter(
//                        List.of()
//                )
//        );
//    }
}
