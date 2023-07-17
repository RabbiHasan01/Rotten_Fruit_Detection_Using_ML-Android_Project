package com.example.mainproject;

public class RatingData {
    private double rating;
    private String userEmail;

    public RatingData() {
        // Empty constructor for Firebase
    }

    public RatingData(double rating, String userEmail) {
        this.rating = rating;
        this.userEmail = userEmail;
    }

    public double getRating() {
        return rating;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
