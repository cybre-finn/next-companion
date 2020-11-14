package com.example.hochi.nextcompanion;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/*
 * Retrofit configuration for the  'Nextbike Rental API'
 */
public class NextbikeRentalServiceGenerator {

    private static final String BASE_URL = "https://api.nextbike.net/api/";

    //Enables Logging of all HTTP-Traffic
    //Set Level to BODY logs all HTTP-Traffic which should be avoided in release for performance and privacy reasons.
    //Level.BASIC logs requests and responses and should be used in release.
    //Level.NONE disables logging.
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            // .setLevel(HttpLoggingInterceptor.Level.BODY); //for debugging
            .setLevel(HttpLoggingInterceptor.Level.BASIC); //for release
            //TODO: Have android studio check if we are in release or debugging.

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
