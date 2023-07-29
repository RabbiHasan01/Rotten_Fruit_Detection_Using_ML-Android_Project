package com.example.mainproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPost extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView buttonChooseImage;
    private EditText editTextCaption;
    Button buttonPostImage;
    private Uri selectedImageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Fruit Detection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //imageView = findViewById(R.id.imageView);
        editTextCaption = findViewById(R.id.editTextCaption);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        buttonPostImage = findViewById(R.id.buttonPostImage);

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        buttonPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    String caption = editTextCaption.getText().toString().trim();
                    postImageWithCaption(caption);
                } else {
                    Toast.makeText(AddPost.this, "Please choose an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                buttonChooseImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void postImageWithCaption(String caption) {
        if (selectedImageUri != null) {
            // Get a reference to the Firebase storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // Create a reference to the image file to be uploaded
            User user = new User();
            String imageName = /* user.getName() +"  "+*/System.currentTimeMillis() + ".jpg"; // Unique image name
            StorageReference imageRef = storageRef.child("images/" + imageName);

            // Upload the image to Firebase storage
            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image upload successful, get the download URL of the image
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Image download URL obtained, now save it along with the caption to Firebase Realtime Database
                            String imageUrl = downloadUri.toString();
                            saveImageCaptionToDatabase(imageUrl, caption);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", "Error getting image download URL: " + e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firebase", "Error uploading image: " + e.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Please choose an image first", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageCaptionToDatabase(String imageUrl, String caption) {
        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("posts");

        Users user =  new Users();

        // Create a new post entry in the database
        String postId = databaseRef.push().getKey(); // Generate a unique post ID
        Map<String, Object> postValues = new HashMap<>();
        //postValues.put("Name", user.getName());
        postValues.put("imageUrl", imageUrl);
        postValues.put("caption", caption);


        // Save the post data to the database
        databaseRef.child(postId).setValue(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Post data saved successfully
                Intent intent = new Intent(AddPost.this,BlogActivity.class);
                startActivity(intent);
                String message = "Image and Caption posted:\n" + caption;
                Toast.makeText(AddPost.this, message, Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "Error saving post data: " + e.getMessage());
            }
        });
    }

}
