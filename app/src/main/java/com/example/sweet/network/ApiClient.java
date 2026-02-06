package com.example.sweet.network;

import android.content.Context;

import com.example.sweet.utils.SessionManager;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static final String BASE_URL = "https://sweet-e8ar.onrender.com/";
    private static volatile Retrofit retrofit;

    private ApiClient() {
        // no instance
    }

    // ✅ ALWAYS call this with Context
    public static Retrofit getClient(Context context) {

        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {

                    Context appContext = context.getApplicationContext();

                    // 🍪 Cookie manager (backend compatibility)
                    CookieManager cookieManager = new CookieManager();
                    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

                    // 📜 Logging (debug only)
                    HttpLoggingInterceptor loggingInterceptor =
                            new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .cookieJar(new JavaNetCookieJar(cookieManager))
                            .addInterceptor(chain -> {

                                Request original = chain.request();
                                Request.Builder builder = original.newBuilder();

                                String token =
                                        new SessionManager(appContext).getToken();

                                if (token != null && !token.isEmpty()) {
                                    builder.addHeader(
                                            "Authorization",
                                            "Bearer " + token
                                    );
                                }

                                return chain.proceed(builder.build());
                            })
                            .addInterceptor(loggingInterceptor)

                            // ⏱ Render cold-start safe
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)

                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )
                            .build();
                }
            }
        }

        return retrofit;
    }
}
