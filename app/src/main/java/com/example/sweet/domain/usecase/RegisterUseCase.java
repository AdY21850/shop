package com.example.sweet.domain.usecase;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.sweet.data.dto.RegisterRequest;
import com.example.sweet.data.dto.LoginResponse;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.AuthApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUseCase {

    private static final String TAG = "RegisterUseCase";

    public final MutableLiveData<LoginResponse> registerResult =
            new MutableLiveData<>();

    private final AuthApi authApi;

    // ✅ CONTEXT-INJECTED CONSTRUCTOR (CORRECT)
    public RegisterUseCase(Context context) {
        authApi = ApiClient
                .getClient(context.getApplicationContext())
                .create(AuthApi.class);
    }

    public void execute(String name, String email, String password) {

        Log.d(TAG, "Register started for: " + email);

        RegisterRequest request =
                new RegisterRequest(name, email, password);

        authApi.register(request).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(
                    Call<LoginResponse> call,
                    Response<LoginResponse> response
            ) {

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Register SUCCESS");
                    registerResult.postValue(response.body());
                } else {
                    Log.e(TAG, "Register FAILED Code: " + response.code());

                    LoginResponse error = new LoginResponse();
                    error.success = false;
                    error.message = "Register failed: " + response.code();

                    registerResult.postValue(error);
                }
            }

            @Override
            public void onFailure(
                    Call<LoginResponse> call,
                    Throwable t
            ) {
                Log.e(TAG, "Register FAILED Network Error", t);

                LoginResponse error = new LoginResponse();
                error.success = false;
                error.message = "Network error";

                registerResult.postValue(error);
            }
        });
    }
}
