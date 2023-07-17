

package com.example.mainproject;


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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private ImageView profileImageView;
    private TextView nameTextView, usernameTextView, emailTextView, ageTextView, genderTextView;
    private Button chooseImageButton, saveButton, downloadButton;

    private Uri imageUri;

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
        ageTextView = findViewById(R.id.ageTextView);
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
                                            String age = ageTextView.getText().toString().trim();
                                            String gender = genderTextView.getText().toString().trim();

                                            User user1= new User(name, username,email, age, gender);

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
                String age = ageTextView.getText().toString().trim();
                String gender = genderTextView.getText().toString().trim();

                User user1= new User(name, username,email, age, gender);

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
/*
    private void createAndSaveProfileAsPDF() {
        String name = nameTextView.getText().toString();
        String username = usernameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String age = ageTextView.getText().toString();
        String gender = genderTextView.getText().toString();

        String filePath = Environment.getExternalStorageDirectory().getPath() + "/ProfilePDFs/Profile.pdf";
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
            com.itextpdf.text.Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 16);

            // Add profile information to the document
            Paragraph title = new Paragraph("Profile Information", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Name: " + name, contentFont));
            document.add(new Paragraph("Username: " + username, contentFont));
            document.add(new Paragraph("Email: " + email, contentFont));
            document.add(new Paragraph("Age: " + age, contentFont));
            document.add(new Paragraph("Gender: " + gender, contentFont));

            Toast.makeText(EditProfile.this, "Profile downloaded as PDF", Toast.LENGTH_SHORT).show();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(EditProfile.this, "Failed to download profile as PDF", Toast.LENGTH_SHORT).show();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
*/

   /*
    private void createAndSaveProfileAsPDF() {



        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameTextView.getText().toString();
                String username = usernameTextView.getText().toString();
                String email = emailTextView.getText().toString();
                String age = ageTextView.getText().toString();
                String gender = genderTextView.getText().toString();

                User user1= new User(name, username,email, age, gender);

                Users updatedUser = new Users( user1, null);

                mAuth = FirebaseAuth.getInstance();
                mDatabase = FirebaseDatabase.getInstance().getReference("Users");

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();
                mDatabase.child(userId).setValue(updatedUser);

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        printPDF();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });



        /*
        //com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size: 595x842 pixels
        //PdfDocument.Page page = document.startPage(pageInfo);

        // Create a canvas for drawing the profile information
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        // Draw the profile information on the canvas
        String name = nameTextView.getText().toString();
        String username = usernameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String age = ageTextView.getText().toString();
        String gender = genderTextView.getText().toString();

        int x = 50; // x-coordinate
        int y = 100; // y-coordinate
        int lineHeight = 30;

        canvas.drawText("Name: " + name, x, y, paint);
        canvas.drawText("Username: " + username, x, y + lineHeight, paint);
        canvas.drawText("Email: " + email, x, y + 2 * lineHeight, paint);
        canvas.drawText("Age: " + age, x, y + 3 * lineHeight, paint);
        canvas.drawText("Gender: " + gender, x, y + 4 * lineHeight, paint);

        document.finishPage(page);

        // Define the file path for saving the PDF
        String directoryPath = Environment.getExternalStorageDirectory().getPath() + "/ProfilePDFs";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = directoryPath + "/Profile.pdf";
        File file = new File(filePath);

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(EditProfile.this, "Profile downloaded as PDF", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(EditProfile.this, "Failed to download profile as PDF", Toast.LENGTH_SHORT).show();
        }

        document.close();
        *

    }*/


    private void printPDF() {

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                String age = ageTextView.getText().toString();
                String gender = genderTextView.getText().toString();

                int x = 20; // x-coordinate
                int y = 80; // y-coordinate

                canvas.drawText("Name: " + name, x, y, paint);
                canvas.drawText("Username: " + username, x, y, paint);
                canvas.drawText("Email: " + email, x, y, paint);
                canvas.drawText("Age: " + age, x, y, paint);
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
                File file = new File(filePath);


                /*try {
                    // Create a FileOutputStream to write the PDF data to the file
                    FileOutputStream fos = new FileOutputStream(file);


                    // Write the PDF data to the FileOutputStream
                    pdfDocument.writeTo(fos);
                    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/profile.pdf";


                    // Create a File object
                    File file = new File(filePath);*/


                    try {
                        // Create a FileOutputStream to write the PDF data to the file
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
                }*/


                    // Close the PdfDocument
                    pdfDocument.close();




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
                }*/
            }

        });
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createAndSaveProfileAsPDF();
        }
    }*/


    /*
    private void generatePDF() {
        // Define the path and file name for the PDF file
        String filePath = Environment.getExternalStorageDirectory().toString() + "/profile.pdf";

        try {
            // Create a new instance of PdfWriter class
            PdfWriter writer = new PdfWriter(filePath);

            // Create a new PDF document
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Open the document for writing
            document.open();

            document.add(new Paragraph("Name: " + nameTextView.getText().toString()));
            document.add(new Paragraph("Username: " + usernameTextView.getText().toString()));
            document.add(new Paragraph("Email: " + emailTextView.getText().toString()));
            document.add(new Paragraph("Age: " + ageTextView.getText().toString()));
            document.add(new Paragraph("Gender: " + genderTextView.getText().toString()));

            // Close the document
            document.close();

            // Show a toast message indicating successful PDF generation
            Toast.makeText(this, "PDF generated successfully", Toast.LENGTH_SHORT).show();

            // Open the PDF file using a PDF viewer app
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent newIntent = Intent.createChooser(intent, "Open File");
            try {
                startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                // Show a toast message if no PDF viewer app is installed
                Toast.makeText(this, "No PDF viewer app found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Show a toast message indicating failure in PDF generation
            Toast.makeText(this, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }
    }
*/

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
                            ageTextView.setText( userProfile.getAge());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
