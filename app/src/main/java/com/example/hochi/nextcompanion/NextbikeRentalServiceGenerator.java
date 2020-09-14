package com.example.hochi.nextcompanion;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/*
 * Retrofit configuration for the  'Nextbike Rental API'
 */
public class NextbikeRentalServiceGenerator {

    private static final String BASE_URL =  "https://api.nextbike.net/api/";

    //Enables Logging of all HTTP-Traffic 
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
            //TODO: Set to Level.BASIC or Level.NONE in release.

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient)
                .build();

    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }
}
