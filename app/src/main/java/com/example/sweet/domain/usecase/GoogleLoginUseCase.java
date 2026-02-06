package com.example.sweet.domain.usecase;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.sweet.data.dto.GoogleTokenRequest;
import com.example.sweet.data.dto.LoginResponse;
import com.example.sweet.network.ApiClient;
import com.example.sweet.network.AuthApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleLoginUseCase {

    public final MutableLiveData<LoginResponse> loginResult =
            new MutableLiveData<>();

    private final AuthApi authApi;

    // ✅ CONTEXT-INJECTED CONSTRUCTOR (CORRECT)
    public GoogleLoginUseCase(Context context) {
        authApi = ApiClient
                .getClient(context.getApplicationContext())
                .create(AuthApi.class);
    }

    // isRegister = false → GOOGLE LOGIN
    // isRegister = true  → GOOGLE REGISTER
    public void execute(String token, boolean isRegister) {

        GoogleTokenRequest request =
                new GoogleTokenRequest(token);

        Call<LoginResponse> call = isRegister
                ? authApi.googleRegister(request)
                : authApi.googleLogin(request);

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(
                    Call<LoginResponse> call,
                    Response<LoginResponse> response
            ) {
                loginResult.postValue(response.body());
            }

            @Override
            public void onFailure(
                    Call<LoginResponse> call,
                    Throwable t
            ) {
                loginResult.postValue(null);
            }
        });
    }
}
