package com.example.sweet.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweet.R;
import com.example.sweet.data.model.Sweet;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.SweetApi;
import com.example.sweet.ui.adapter.ProductListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesList extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private TextView pageTitle;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories_list);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        pageTitle = findViewById(R.id.pageTitle);
        backButton = findViewById(R.id.backButton);

        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        pageTitle.setText(categoryName);
        backButton.setOnClickListener(v -> finish());

        productRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        fetchSweets(categoryName);
    }

    private void fetchSweets(String categoryName) {

        SweetApi api = ApiClient.getClient(this).create(SweetApi.class);

        api.getSweetsByCategory(categoryName)
                .enqueue(new Callback<List<Sweet>>() {

                    @Override
                    public void onResponse(
                            Call<List<Sweet>> call,
                            Response<List<Sweet>> response
                    ) {
                        if (!response.isSuccessful() || response.body() == null) return;

                        productRecyclerView.setAdapter(
                                new ProductListAdapter(
                                        CategoriesList.this,
                                        response.body()
                                )
                        );
                    }

                    @Override
                    public void onFailure(Call<List<Sweet>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
