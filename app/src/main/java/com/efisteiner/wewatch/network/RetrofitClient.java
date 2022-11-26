package com.efisteiner.wewatch.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitClient {
    public static final String API_KEY ="d058bcafe8f19acd050976b5add86260";
    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    private static RetrofitClient instance;
    private static ApiInterface api;

    private RetrofitClient() {
       Retrofit retrofit = new
               Retrofit.Builder().baseUrl(TMDB_BASE_URL)
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .addConverterFactory(GsonConverterFactory.create())
               .build();

       api = retrofit.create(ApiInterface.class);

    }

    public static synchronized RetrofitClient getInstance() {
       if(instance == null) {
           instance = new RetrofitClient();
       }

       return instance;
    }

    public ApiInterface getApi() {
        return api;
    }

}
