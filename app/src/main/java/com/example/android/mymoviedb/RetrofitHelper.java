package com.example.android.mymoviedb;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Archita on 03-08-2017.
 */

public class RetrofitHelper {

    private ApiInterface mAPI;

    public RetrofitHelper(String baseURL) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mAPI = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getAPI() {
        return mAPI;
    }

}
