package com.example.mainproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mainproject.CommentsActivity;

public class BlogActivity extends AppCompatActivity {
    private int likeCount = 10;
    private int commentCount = 5;
    private Button likeButton;
    private Button commentButton;
    private TextView likeCountTextView;
    private TextView commentCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        // Initialize views
        likeButton = findViewById(R.id.likeButton);
        commentButton = findViewById(R.id.commentButton);
        likeCountTextView = findViewById(R.id.likeCountTextView);
        commentCountTextView = findViewById(R.id.commentCountTextView);

        // Set initial like and comment counts
        updateLikeCount();
        updateCommentCount();

        // Set click listeners for buttons
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseLikeCount();
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCommentsActivity();
            }
        });
    }

    private void increaseLikeCount() {
        likeCount++;
        updateLikeCount();
    }

    private void updateLikeCount() {
        likeCountTextView.setText(String.valueOf(likeCount) + " Likes");
    }

    private void updateCommentCount() {
        commentCountTextView.setText(String.valueOf(commentCount) + " Comments");
    }

    private void openCommentsActivity() {
        Intent intent = new Intent(BlogActivity.this, CommentsActivity.class);
        startActivity(intent);
    }
}
