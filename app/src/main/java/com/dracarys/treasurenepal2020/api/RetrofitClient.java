package com.dracarys.treasurenepal2020.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by praka on 12/24/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit;

    //AWS SERVER IP ADDRESS
    //This will probably be down by the time you test it due to 1 hr limit. If any problem arise, please use the local address instead.
    //private static final String BASE_URL = "http://18.214.223.15:8000/";

    //FOR LOCAL TESTING
    private static final String BASE_URL = "http://10.16.2.63:8000/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}