package com.example.mainproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText signUpName,signUpUsername,signUpEmail,signUpBirthdate,signUpPhone,signUpGender,signUpPassword,signUpConfirmPassword;
    private Button signUp;
    private ProgressBar signUpProgressBar;
    private RadioGroup radioGroup;

    private ImageButton birthdatePicker;
    private Spinner genderSpinner;
    private Spinner divisionSpinner;
    private Spinner districtSpinner;
    private AddressMap addressMap;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    Gender gender;
    Boolean flag;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Fruit Detection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        signUpName = findViewById(R.id.signUpNameEditTextId);
        signUpUsername = findViewById(R.id.signUpUsernameEditTextId);
        signUpEmail = findViewById(R.id.signUpEmailEditTextId);
        /*signUpPhone = findViewById(R.id.signUpPhoneEditTextId);
        signUpAddress = findViewById(R.id.signUpAddressEditTextId);*/

        divisionSpinner = findViewById(R.id.signUpSpinnerDivision);
        districtSpinner = findViewById(R.id.signUpSpinnerDistrict);

        signUpBirthdate = findViewById(R.id.signUpBirthdateEditTextId);
        birthdatePicker= findViewById(R.id.birthdatePickerButton);

        signUpPhone = findViewById(R.id.signUpPhoneEditTextId);
        signUpGender = findViewById(R.id.signUpGenderEditTextId);
        genderSpinner = findViewById(R.id.spinnerId);
        signUpPassword = findViewById(R.id.signUpPasswordEditTextId);
        signUpConfirmPassword = findViewById(R.id.signUpConfirmPasswordEditTextId);
        signUp = findViewById(R.id.signUpButtonId);
        signUpProgressBar = findViewById(R.id.signUpProgressBarId);

        /*RadioButton genderBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        gender = genderBtn.getText().toString();*/

        birthdatePicker.setOnClickListener(this);
        Address();
        SelectGender();


        signUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.birthdatePickerButton) {
            showDatePickerDialog();
        }
        if(view.getId()==R.id.signUpButtonId){
            registerUser();
        }
    }

    private void Address() {
        // Initialize AddressMap
        addressMap = new AddressMap();

        // Set up division spinner
        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(addressMap.getDivisionDistrictMap().keySet()));
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(divisionAdapter);

        // Set up district spinner
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDivision = divisionSpinner.getSelectedItem().toString();
                List<String> districtList = addressMap.getDivisionDistrictMap().get(selectedDivision);

                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(SignUp.this,
                        android.R.layout.simple_spinner_item, districtList);
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                districtSpinner.setAdapter(districtAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                signUpBirthdate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void SelectGender() {

        gender=new Gender();

        reference=firebaseDatabase.getInstance().getReference().child("Spinner");


        List<String> categories= new ArrayList<>();
        categories.add(0,"Select gender");
        categories.add(1,"Male");
        categories.add(2,"Female");
        //categories.add("Transgender");

        ArrayAdapter<String> dataAdapter;
        dataAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderSpinner.setAdapter(dataAdapter);


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(parent.getItemAtPosition(position).equals("Select gender")){
                    //selected.setText(parent.getSelectedItem().toString());
                    flag=false;
                }else{
                    flag=true;
                    signUpGender.setText(parent.getSelectedItem().toString());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                signUpGender.setError("Field must be selected.");
            }
        });

    }



    public void registerUser() {
        String name = signUpName.getText().toString().trim();
        String username = signUpUsername.getText().toString().trim();
        String email = signUpEmail.getText().toString().trim();
        String birthdate = signUpBirthdate.getText().toString().trim();
        String division = divisionSpinner.getSelectedItem().toString().trim();
        String district = districtSpinner.getSelectedItem().toString().trim();
        String phone = signUpPhone.getText().toString().trim();
        String gender = signUpGender.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();
        String confirm_pass = signUpConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            signUpName.setError("Name is required.");
            signUpName.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            signUpUsername.setError("Username is required.");
            signUpUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            signUpEmail.setError("Email is required.");
            signUpEmail.requestFocus();
            return;
        }

        // Check if the email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmail.setError("Invalid email address.");
            signUpEmail.requestFocus();
            return;
        }

        if (birthdate.isEmpty()) {
            signUpEmail.setError("Birthdate is required.");
            signUpEmail.requestFocus();
            return;
        }

        if (division.isEmpty()) {
            signUpEmail.setError("Division is required.");
            signUpEmail.requestFocus();
            return;
        }

        if (district.isEmpty()) {
            signUpEmail.setError("District is required.");
            signUpEmail.requestFocus();
            return;
        }

        // Check if the age is provided
        if (phone.isEmpty()) {
            signUpPhone.setError("Phone number is required.");
            signUpPhone.requestFocus();
            return;
        }

        if (phone.length()<11 && !phone.startsWith("01")) {
            signUpPhone.setError("Enter valid password!!");
            signUpPhone.requestFocus();
            return;
        }

        // Check if the gender is provided
        if (gender.isEmpty()) {
            signUpGender.setError("Gender is required.");
            signUpGender.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            signUpPassword.setError("Password is required.");
            signUpPassword.requestFocus();
            return;
        }

        if (password.length() <= 6) {
            signUpPassword.setError("Minimum password length should be 6.");
            signUpPassword.requestFocus();
            return;
        }

        if (confirm_pass.isEmpty()) {
            signUpConfirmPassword.setError("Password confirmation is required.");
            signUpConfirmPassword.requestFocus();
            return;
        }

        if (!confirm_pass.equals(password)) {
            signUpConfirmPassword.setError("Passwords do not match.");
            signUpConfirmPassword.requestFocus();
            return;
        }

        signUpProgressBar.setVisibility(View.VISIBLE);

        // Check username availability
        checkUsernameAvailability(username);
    }

    // Unique username verification
    private void checkUsernameAvailability(String username) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username already exists
                    signUpUsername.setError("Username already exists.");
                    signUpUsername.requestFocus();
                } else {
                    // Username is available, proceed with registration
                    signUpUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Toast.makeText(getApplicationContext(), "Error checking username availability", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Sign up the user with Firebase Authentication and store data in Firebase Database
    private void signUpUser() {
        String name = signUpName.getText().toString().trim();
        String username = signUpUsername.getText().toString().trim();
        String email = signUpEmail.getText().toString().trim();
        String birthdate = signUpBirthdate.getText().toString().trim();
        String division = divisionSpinner.getSelectedItem().toString().trim();
        String district = districtSpinner.getSelectedItem().toString().trim();
        String phone = signUpPhone.getText().toString().trim();
        String gender = signUpGender.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(name, username, email, birthdate, division, district, phone, gender);

                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(SignUp.this, "Registration completed. Please verify email address.", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(SignUp.this, AppFeatures.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                        }
                                                        signUpProgressBar.setVisibility(View.GONE);
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                        signUpProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                            signUpProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    /* */
}