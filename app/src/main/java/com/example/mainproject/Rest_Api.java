package com.example.mainproject;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rest_Api extends AppCompatActivity {

    private TextView postsContentTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_api);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Rest Api");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postsContentTextView = findViewById(R.id.postsContentTextView);

        // Fetch posts from the API
        ApiService.getPosts(new Callback<List<PostApi>>() {
            @Override
            public void onResponse(Call<List<PostApi>> call, Response<List<PostApi>> response) {
                if (response.isSuccessful()) {
                    List<PostApi> posts = response.body();

                    // Process the list of posts and display them in the TextView
                    StringBuilder postsText = new StringBuilder();
                    for (PostApi post : posts) {
                        postsText.append("Title: ").append(post.getTitle()).append("\n");
                        // You can add more fields to display (e.g., content, author, etc.)
                        postsText.append("--------------\n");
                    }

                    postsContentTextView.setText(postsText.toString());
                } else {
                    postsContentTextView.setText("Failed to get posts. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PostApi>> call, Throwable t) {
                postsContentTextView.setText("Failed to get posts. Error: " + t.getMessage());
            }
        });
    }
}
