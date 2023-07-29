package com.example.mainproject;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LikeHandler {
    private String postId;
    private FirebaseUser currentUser;
    private boolean isLiked;
    private int likeCount;

    public LikeHandler(String postId, FirebaseUser currentUser) {
        this.postId = postId;
        this.currentUser = currentUser;
        this.isLiked = false;
        this.likeCount = 0;

        // Initialize the LikeHandler by fetching the like status and count from the database
        DatabaseReference likesReference = FirebaseDatabase.getInstance().getReference().child("UserLikes").child(currentUser.getEmail().replace(".", "-"));
        likesReference.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isLiked = snapshot.getValue(Boolean.class);
                } else {
                    isLiked = false;
                }
                updateLikeCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    public boolean isLiked() {
        return isLiked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void toggleLikeStatus() {
        DatabaseReference userLikesReference = FirebaseDatabase.getInstance().getReference().child("UserLikes").child(currentUser.getEmail().replace(".", "-")).child(postId);
        DatabaseReference likesReference = FirebaseDatabase.getInstance().getReference().child("TotalLikes");

        if (isLiked) {
            userLikesReference.setValue(false);
            likesReference.setValue(likeCount - 1);
            likeCount--;
        } else {
            userLikesReference.setValue(true);
            likesReference.setValue(likeCount + 1);
            likeCount++;
        }
        isLiked = !isLiked;
    }

    private void updateLikeCount() {
        DatabaseReference likesReference = FirebaseDatabase.getInstance().getReference().child("TotalLikes");
        likesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    likeCount = snapshot.getValue(Integer.class);
                } else {
                    likeCount = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}
