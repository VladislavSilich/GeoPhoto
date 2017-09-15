package com.geophotos.example.silich.vladislav.geophotosrepository.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lenovo on 11.09.2017.
 */

public class ServiceGenerator {
    private static String BASE_URL = "http://213.184.248.43:9099/api/";
    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder sBuilder = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass){
        Retrofit retrofit = sBuilder.client(httpClient.build())
                .build();
        return retrofit.create(serviceClass);
    }
}
