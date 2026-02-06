package com.example.sweet.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweet.R;
import com.example.sweet.data.model.Category;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.CategoryApi;
import com.example.sweet.ui.adapter.CategoryGridAdapter;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        RecyclerView recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        CategoryApi api =
                ApiClient.getClient(this).create(CategoryApi.class);

        api.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(
                    Call<List<Category>> call,
                    Response<List<Category>> response
            ) {
                String json = new Gson().toJson(response.body());
                android.util.Log.e("CATEGORY_API", "✅ Response JSON = " + json);

                recyclerView.setAdapter(
                        new CategoryGridAdapter(
                                Categories.this,
                                response.body()
                        )
                );
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            }
        });
    }
}
