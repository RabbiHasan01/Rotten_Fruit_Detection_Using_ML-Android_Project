package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Like extends AppCompatActivity {

    private Button likeButton;
    private TextView totalLikesTextView;

    private DatabaseReference likesReference;
    private DatabaseReference userLikesReference;
    private FirebaseAuth mAuth;

    private int totalLikes = 0;
    private boolean hasLiked = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        likeButton = findViewById(R.id.likeButton);
        totalLikesTextView = findViewById(R.id.totalLikesTextView);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        likesReference = FirebaseDatabase.getInstance().getReference().child("TotalLikes");
        userLikesReference = FirebaseDatabase.getInstance().getReference().child("UserLikes").child(userId);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasLiked) {
                    userLikesReference.setValue(false);
                    likesReference.setValue(totalLikes - 1);
                    totalLikes--;
                } else {
                    userLikesReference.setValue(true);
                    likesReference.setValue(totalLikes + 1);
                    totalLikes++;
                }
                hasLiked = !hasLiked;
                updateLikeButtonState();
            }
        });

        userLikesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    hasLiked = snapshot.getValue(Boolean.class);
                } else {
                    hasLiked = false;
                }
                updateLikeButtonState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        likesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    totalLikes = snapshot.getValue(Integer.class);
                } else {
                    totalLikes = 0;
                }
                updateLikesCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void updateLikeButtonState() {
        if (hasLiked) {
            likeButton.setText("Unlike");
        } else {
            likeButton.setText("Like");
        }
    } /*
    private void updateLikeButtonState() {
        if (hasLiked) {
            likeButton.setBackgroundResource(R.drawable.dislike_img);
        } else {
            likeButton.setBackgroundResource(R.drawable.like_img);
        }
    }*/


    private void updateLikesCount() {
        totalLikesTextView.setText(""+totalLikes);
    }
}
