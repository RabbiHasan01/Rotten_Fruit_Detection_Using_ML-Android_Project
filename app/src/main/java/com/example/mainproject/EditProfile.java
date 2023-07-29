

package com.example.mainproject;


import static android.os.Environment.getExternalStorageDirectory;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
/*import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import android.Manifest;
import android.content.pm.PackageManager;
//import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;

*/
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private ImageView profileImageView;
    private TextView nameTextView, usernameTextView, emailTextView, phoneTextView, genderTextView;
    private Button chooseImageButton, saveButton, downloadButton;

    private Uri imageUri;

    private static final int SAVE_PDF_REQUEST_CODE = 123; // Any unique request code

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mStorage = FirebaseStorage.getInstance().getReference("ProfileImages");

        profileImageView = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.nameTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        genderTextView = findViewById(R.id.genderTextView);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        saveButton = findViewById(R.id.saveButton);

        downloadButton= findViewById(R.id.downloadButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        downloadButton = findViewById(R.id.downloadButton);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Check runtime permission for writing external storage (required for saving PDF)
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        createAndSaveProfileAsPDF();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    createAndSaveProfileAsPDF();
                }*/
                printPDF();
            }
        });

        loadProfileData();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void saveChanges() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (imageUri != null) {
                StorageReference imageRef = mStorage.child(user.getUid() + ".jpg");

                imageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri uri = task.getResult();

                                        if (uri != null) {
                                            String name = nameTextView.getText().toString().trim();
                                            String username = usernameTextView.getText().toString().trim();
                                            String email = emailTextView.getText().toString().trim();
                                            String phone = phoneTextView.getText().toString().trim();
                                            String gender = genderTextView.getText().toString().trim();

                                            User user1= new User(name, username,email,null, null,null, phone, gender); //birthdate+div+dis include hbe

                                            Users updatedUser = new Users( user1 ,uri.toString());

                                            mDatabase.child(user.getUid()).setValue(updatedUser, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError == null) {
                                                        Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                        //generatePDF();
                                                        //createAndSaveProfileAsPDF();
                                                        //printPDF();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(EditProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(EditProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(EditProfile.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(EditProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                String name = nameTextView.getText().toString().trim();
                String username = usernameTextView.getText().toString().trim();
                String email = emailTextView.getText().toString().trim();
                String phone = phoneTextView.getText().toString().trim();
                String gender = genderTextView.getText().toString().trim();

                User user1= new User(name, username,email,null,null,null, phone, gender); //birthdate+div+dist lagbe

                Users updatedUser = new Users( user1, null);

                mDatabase.child(user.getUid()).setValue(updatedUser, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            //generatePDF();
                            //createAndSaveProfileAsPDF();
                            //printPDF();
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(EditProfile.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    private void printPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint forLinePaint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size: 595x842 pixels
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Create a canvas for drawing the profile information
        Canvas canvas = page.getCanvas();
        paint.setTextSize(20);
        paint.setColor(Color.rgb(0, 50, 250));

        canvas.drawText("Fruit Detection", 20, 20, paint);
        paint.setTextSize(8.5f);

        canvas.drawText("University of Chittagong", 20, 40, paint);
        canvas.drawText("Chittagong-4015", 20, 55, paint);

        forLinePaint.setStyle(Paint.Style.STROKE);
        forLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        forLinePaint.setStrokeWidth(2);
        canvas.drawLine(20, 65, 575, 65, forLinePaint); // Adjusted the line length to cover the entire page

        // Draw the profile information on the canvas
        String name = nameTextView.getText().toString();
        String username = usernameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String phone = phoneTextView.getText().toString();
        String gender = genderTextView.getText().toString();

        int x = 20; // x-coordinate
        int y = 80; // y-coordinate
        int lineHeight = 20; // Adjust the line height for each text

        canvas.drawText("Name: " + name, x, y, paint);
        y += lineHeight;
        canvas.drawText("Username: " + username, x, y, paint);
        y += lineHeight;
        canvas.drawText("Email: " + email, x, y, paint);
        y += lineHeight;
        canvas.drawText("Age: " + phone, x, y, paint);
        y += lineHeight;
        canvas.drawText("Gender: " + gender, x, y, paint);

        y += 20; // Add some spacing

        canvas.drawLine(20, y, 575, y, forLinePaint); // Draw a horizontal line below the profile information

        y += 20; // Add some spacing

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        canvas.drawText("Date: " + simpleDateFormat.format(new Date().getTime()), 20, y, paint);

        y += 40; // Add some spacing

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText("Thank You!", canvas.getWidth() / 2, y, paint);

        pdfDocument.finishPage(page);

        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "FruitDetection");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, "profile.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            pdfDocument.close();
            Toast.makeText(EditProfile.this, "profile downloaded as pdf", Toast.LENGTH_SHORT).show();

            // Use SAF to let the user save the PDF file in their desired location
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_TITLE, "profile.pdf");
            startActivityForResult(intent, SAVE_PDF_REQUEST_CODE); // 123,any num
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(EditProfile.this, "failed to download as pdf", Toast.LENGTH_SHORT).show();
        }
    }

    /*private void printPDF2() {

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint forLinePaint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size: 595x842 pixels
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);


        // Create a canvas for drawing the profile information
        Canvas canvas = page.getCanvas();
        paint.setTextSize(20);

        paint.setColor(Color.rgb(0, 50, 250));

        canvas.drawText("Fruit Detection", 20, 20, paint);
        paint.setTextSize(8.5f);

        canvas.drawText("University of Chittagong", 20, 40, paint);
        canvas.drawText("Chittagong-4015", 20, 55, paint);

        forLinePaint.setStyle(Paint.Style.STROKE);
        forLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        forLinePaint.setStrokeWidth(2);
        canvas.drawLine(20, 65, 230, 65, forLinePaint);


        // Draw the profile information on the canvas
        String name = nameTextView.getText().toString();
        String username = usernameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String phone = phoneTextView.getText().toString();
        String gender = genderTextView.getText().toString();

        int x = 20; // x-coordinate
        int y = 80; // y-coordinate

        canvas.drawText("Name: " + name, x, y, paint);
        canvas.drawText("Username: " + username, x, y, paint);
        canvas.drawText("Email: " + email, x, y, paint);
        canvas.drawText("Age: " + phone, x, y, paint);
        canvas.drawText("Gender: " + gender, x, y, paint);

        canvas.drawLine(20, 65, 230, 65, forLinePaint);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

        canvas.drawText("date: " + simpleDateFormat.format(new Date().getTime()), 20, 260, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);

        canvas.drawText("Thank You!", canvas.getHeight() / 2, 320, paint);

        pdfDocument.finishPage(page);


        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/profile.pdf";


        // Create a File object
        File file = new File(filePath);*/


                /*try {
                    // Create a FileOutputStream to write the PDF data to the file
                    FileOutputStream fos = new FileOutputStream(file);


                    // Write the PDF data to the FileOutputStream
                    pdfDocument.writeTo(fos);
                    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/profile.pdf";


                    // Create a File object
                    File file = new File(filePath);*


                    try {
                        / Create a FileOutputStream to write the PDF data to the file
                        FileOutputStream fos = new FileOutputStream(file);


                        // Write the PDF data to the FileOutputStream
                        pdfDocument.writeTo(fos);

                        // Close the FileOutputStream
                        fos.close();


                        // Show a Toast message indicating that the PDF was downloaded successfully
                        Toast.makeText(EditProfile.this, "PDF downloaded successfully", Toast.LENGTH_SHORT).show();


                        // Open the downloaded PDF file using an Intent
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Intent chooserIntent = Intent.createChooser(intent, "Open with");
                        try {
                            startActivity(chooserIntent);
                        } catch (ActivityNotFoundException e) {
                            // Show a Toast message if no PDF viewer app is installed
                            Toast.makeText(EditProfile.this, "No PDF viewer app found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Show a Toast message indicating that the PDF download failed
                        Toast.makeText(EditProfile.this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
                    }
                /*} catch (IOException e) {
                    e.printStackTrace();
                    // Show a Toast message indicating that the PDF download failed
                    Toast.makeText(EditProfile.this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
                }*


                    // Close the PdfDocument
                    //pdfDocument.close();




                /*File directory = new File(Environment.getExternalStorageDirectory(), "Fruit Detection");

                if(!directory.exists()){
                    directory.mkdirs();
                }


                File file = new File(directory,"FruitDetection/profile.pdf");



                try {
                    pdfDocument.writeTo(new FileOutputStream(file));
                    pdfDocument.close();
                    Toast.makeText(EditProfile.this, "profile downloaded as pdf", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                    e.printStackTrace();
                    Toast.makeText(EditProfile.this, "failed to download as pdf", Toast.LENGTH_SHORT).show();
                }*
    }*/

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createAndSaveProfileAsPDF();
        }
    }
    }*/

    private void loadProfileData() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Users userProfile = dataSnapshot.getValue(Users.class);

                        if (userProfile != null) {
                            nameTextView.setText(userProfile.getName());
                            usernameTextView.setText(userProfile.getUsername());
                            emailTextView.setText(userProfile.getEmail());
                            phoneTextView.setText(userProfile.getPhone());
                            genderTextView.setText(userProfile.getGender());

                            if (userProfile.getProfileImageURL() != null) {
                                Glide.with(EditProfile.this)
                                        .load(userProfile.getProfileImageURL())
                                        .placeholder(R.drawable.placeholder_image)
                                        .error(R.drawable.placeholder_image)
                                        .into(profileImageView);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditProfile.this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(EditProfile.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Your existing code for handling the image selection
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == SAVE_PDF_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Code for handling the result of the PDF save using SAF
            Uri uri = data.getData();
            try {
                File directory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "FruitDetection");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                File file = new File(directory, "profile.pdf");

                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fileInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                    fileInputStream.close();
                    Toast.makeText(EditProfile.this, "PDF saved successfully!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(EditProfile.this, "Failed to save PDF!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
