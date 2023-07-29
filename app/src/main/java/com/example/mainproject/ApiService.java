
package com.example.mainproject;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/"; //"https://api.nal.usda.gov/fdc/v1/foods/search?query=apple&api_key=DbgMprRBxmT8jr17474FwM8tIPd82R7Ikfp6pA05"; //"https://jsonplaceholder.typicode.com/";
    private static ApiInterface apiInterface;

    public static ApiInterface getRetrofit() {
        if (apiInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
        }

        return apiInterface;
    }

    public static void getPosts(Callback<List<PostApi>> callback) {
        Call<List<PostApi>> call = getRetrofit().getPosts();

        call.enqueue(new Callback<List<PostApi>>() {
            @Override
            public void onResponse(Call<List<PostApi>> call, Response<List<PostApi>> response) {
                if (response.isSuccessful()) {
                    List<PostApi> posts = response.body();
                    callback.onResponse(call, Response.success(posts));
                } else {
                    callback.onFailure(call, new Throwable("Error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<PostApi>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
