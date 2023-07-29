package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText signInEmail,signInPassword;
    private Button signInButton;
    private ProgressBar signInProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Fruit Detection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        signInEmail=findViewById(R.id.signInEmailEditTextId);
        signInPassword=findViewById(R.id.signInPasswordEditTextId);
        signInButton=findViewById(R.id.signInButtonId);
        signInProgressBar=findViewById(R.id.signInProgressBarId);

        register= findViewById(R.id.signInRegisterId);

        register.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.signInRegisterId){
            Intent intent= new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.signInButtonId){

            userLogIn();
        }
    }

    private void userLogIn() {

        String email=signInEmail.getText().toString().trim();
        String password=signInPassword.getText().toString().trim();

        if(email.isEmpty()){
            signInEmail.setError("Email is required.");
            signInEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signInEmail.setError("wrong email address.");
            signInEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            signInPassword.setError("Password is required.");
            signInPassword.requestFocus();
            return;
        }
        if(password.length()<=6){
            signInPassword.setError("Minimum length should be 6.");
            signInPassword.requestFocus();
            return;
        }

        signInProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Intent intent= new Intent(LogIn.this,AppFeatures.class);
                                startActivity(intent);
                                signInProgressBar.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(LogIn.this, "Enter logged-in email address.", Toast.LENGTH_SHORT).show();
                                signInProgressBar.setVisibility(View.GONE);
                            }

                        }else{
                            Toast.makeText(LogIn.this, "Sign In failed. Try again!!.", Toast.LENGTH_SHORT).show();
                            signInProgressBar.setVisibility(View.GONE);
                        }

                    }

                });
    }

    //session info activity
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is already signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startHomeActivity();
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LogIn.this, AppFeatures.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}