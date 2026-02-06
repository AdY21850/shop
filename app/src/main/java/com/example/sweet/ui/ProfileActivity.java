package com.example.sweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sweet.R;
import com.example.sweet.data.model.User;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.ProfileApi;
import com.example.sweet.utils.SessionManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView userName, userEmail;
    private LinearLayout navHome, navOrders, navCart, navProfile;
    private Button btnLogout;

    private SessionManager sessionManager;

    private static final String TAG = "PROFILE_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);


        findViewById(R.id.btnlogout).setOnClickListener(v -> {

            // Clear session
            sessionManager.clearSession();

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        bindViews();
        setupBottomNav();
        fetchProfile();
    }

    // =========================
    // BIND VIEWS
    // =========================
    private void bindViews() {
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);

        navHome = findViewById(R.id.navHome);
        navOrders = findViewById(R.id.navOrders);
        navCart = findViewById(R.id.navCart);
        navProfile = findViewById(R.id.navProfile);
    }

    // =========================
    // FETCH PROFILE
    // =========================
    private void fetchProfile() {

        ProfileApi api =
                ApiClient.getClient(this).create(ProfileApi.class);

        api.getProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(
                    Call<User> call,
                    Response<User> response
            ) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e(TAG, "❌ Failed | Code = " + response.code());
                    return;
                }

                // ✅ FULL RESPONSE LOG
                Log.e(TAG, "✅ Profile JSON = " +
                        new Gson().toJson(response.body()));

                bindProfile(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "❌ Network error", t);
            }
        });
    }

    // =========================
    // BIND PROFILE DATA
    // =========================
    private void bindProfile(User user) {

        userName.setText(user.getFullName());
        userEmail.setText(user.getEmail());

        Glide.with(this)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(profileImage);

        // ✨ subtle pop animation
        profileImage.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(250)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() ->
                        profileImage.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(150)
                                .start()
                ).start();
    }

    // =========================
    // BOTTOM NAVIGATION
    // =========================
    private void setupBottomNav() {

        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, HomeActivity.class)));

        navOrders.setOnClickListener(v ->
                Toast.makeText(this, "Orders coming soon", Toast.LENGTH_SHORT).show());

        navCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        navProfile.setOnClickListener(v -> {
            // already here – do nothing
        });
    }


}
