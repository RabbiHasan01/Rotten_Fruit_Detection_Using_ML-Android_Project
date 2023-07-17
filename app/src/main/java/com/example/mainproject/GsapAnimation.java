package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

public class GsapAnimation extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsap_animation);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Fruit Detection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript (optional)

        // Load an HTML file from the assets folder
        webView.loadUrl("file:///android_asset/gsap.html");

        // Create a handler
        Handler handler = new Handler();

        // Define a delay of 5 seconds (5000 milliseconds)
        int delay = 3000;

        // Define the action to be performed after the delay
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Start the next activity or perform any desired action
                Intent nextActivityIntent = new Intent(GsapAnimation.this, LogIn.class);
                startActivity(nextActivityIntent);
                finish(); // Optional: Close the current activity if needed
            }
        };

        // Post the runnable to be executed after the delay
        handler.postDelayed(runnable, delay);
    }
}
