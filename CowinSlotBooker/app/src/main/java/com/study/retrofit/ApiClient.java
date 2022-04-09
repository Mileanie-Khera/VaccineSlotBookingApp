package com.study.retrofit;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final int DISK_CACHE_SIZE = 10 * 1024 * 1024;

    private static Retrofit retrofit = null;

    public static synchronized Retrofit getClient(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://cdn-api.co-vin.in/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient(context))
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient provideOkHttpClient(final Context context) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(loggingInterceptor);

        return okHttpClientBuilder.build();
    }
}
