package com.example.mainproject;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Like {

    public static void likePost(String postId, FirebaseUser currentUser) {
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        likesRef.child(postId).child(currentUser.getUid()).setValue(true);
    }

    public static void unlikePost(String postId, FirebaseUser currentUser) {
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        likesRef.child(postId).child(currentUser.getUid()).removeValue();
    }

    public static void checkIfLiked(String postId, FirebaseUser currentUser, final OnLikeStatusListener listener) {
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        likesRef.child(postId).child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isLiked = dataSnapshot.exists();
                listener.onLikeStatusReceived(isLiked);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError();
            }
        });
    }

    public interface OnLikeStatusListener {
        void onLikeStatusReceived(boolean isLiked);
        void onError();
    }
}
