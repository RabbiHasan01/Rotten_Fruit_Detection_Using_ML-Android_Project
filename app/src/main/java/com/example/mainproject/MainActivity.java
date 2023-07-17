package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button logInButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Fruit Detection");

        logInButton = findViewById(R.id.login_Button);
        signUpButton = findViewById(R.id.signUp_Button);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.login_Button) {
                    /*Toast.makeText(MainActivity.this, "Log In is Clicked.", Toast.LENGTH_SHORT).show();*/
                    Intent intent = new Intent(MainActivity.this, GsapAnimation.class);
                    startActivity(intent);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.signUp_Button) {
                    /*Toast.makeText(MainActivity.this, "Sign Up is Clicked.", Toast.LENGTH_SHORT).show();*/
                    Intent intent = new Intent(MainActivity.this, SignUp.class);
                    startActivity(intent);
                }
            }
        });

        // Create a handler
       /* Handler handler = new Handler();

        // Define a delay of 5 seconds (5000 milliseconds)
        int delay = 1000;

        // Define the action to be performed after the delay
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Start the next activity or perform any desired action
                Intent nextActivityIntent = new Intent(MainActivity.this, LogIn.class);
                startActivity(nextActivityIntent);
                finish(); // Optional: Close the current activity if needed
            }
        };

        // Post the runnable to be executed after the delay
        handler.postDelayed(runnable, delay);*/
    }
}
