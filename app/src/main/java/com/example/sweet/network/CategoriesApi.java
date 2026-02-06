package com.example.sweet.network;

import com.example.sweet.data.model.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesApi {

    @GET("/api/categories")
    Call<List<Category>> getAllCategories();
}
