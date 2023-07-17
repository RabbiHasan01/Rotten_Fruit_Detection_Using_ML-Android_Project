package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button submitButton;
    private TextView totalRatingTextView, averageRatingTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference ratingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Fruit Detection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmailAddress = currentUser.getEmail();

        ratingsRef = FirebaseDatabase.getInstance().getReference("ratings");

        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitButton);
        totalRatingTextView = findViewById(R.id.totalRatingTextView);
        averageRatingTextView = findViewById(R.id.averageRatingTextView);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                saveRatingToFirebase(rating, userEmailAddress);
            }
        });

        // Retrieve and display total and average ratings from Firebase
        retrieveTotalRating();
        retrieveAverageRating();
    }

    private void saveRatingToFirebase(float rating, String userEmail) {
        RatingData ratingData = new RatingData(rating, userEmail);
        ratingsRef.push().setValue(ratingData);
        ratingBar.setRating(0);
        Toast.makeText(this, "Rating saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void retrieveTotalRating() {
        ratingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalRating = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RatingData ratingData = snapshot.getValue(RatingData.class);
                    totalRating += ratingData.getRating();
                }
                totalRatingTextView.setText("Total Rating: " + totalRating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void retrieveAverageRating() {
        ratingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalRating = 0;
                int numRatings = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RatingData ratingData = snapshot.getValue(RatingData.class);
                    totalRating += ratingData.getRating();
                    numRatings++;
                }
                double averageRating = (double) totalRating / numRatings;
                averageRatingTextView.setText("Average Rating: " + averageRating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
