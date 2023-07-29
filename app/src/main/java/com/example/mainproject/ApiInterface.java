

package com.example.mainproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("posts") // The endpoint path after the BASE_URL
    Call<List<PostApi>> getPosts();
    // Add more methods for other API endpoints if needed
}
