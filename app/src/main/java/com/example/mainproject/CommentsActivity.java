 package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
public class CommentsActivity extends AppCompatActivity {

    private EditText commentEditText;
    private Button postButton;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private DatabaseReference commentsRef;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Fruit Detection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commentEditText = findViewById(R.id.commentEditText);
        postButton = findViewById(R.id.postButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentsRecyclerView.setLayoutManager(layoutManager);
        commentsRecyclerView.setAdapter(commentAdapter);

        commentsRef = FirebaseDatabase.getInstance().getReference("comments");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });

        loadComments();
    }

    private void postComment() {
        String commentText = commentEditText.getText().toString().trim();

        if (commentText.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = currentUser.getEmail();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Comment comment = new Comment(userEmail, currentTime, commentText);
        String commentId = commentsRef.push().getKey();
        commentsRef.child(commentId).setValue(comment);

        commentEditText.setText("");
        Toast.makeText(this, "Comment posted successfully", Toast.LENGTH_SHORT).show();
    }

    private void loadComments() {
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CommentsActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

 public class CommentsActivity extends AppCompatActivity {

     private EditText commentEditText;
     private Button postButton;
     private RecyclerView commentsRecyclerView;
     private CommentAdapter commentAdapter;
     private List<Comment> commentList;
     private DatabaseReference commentsRef;
     private DatabaseReference usersRef;
     private FirebaseUser currentUser;

     @SuppressLint("MissingInflatedId")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_comments);

         getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
         getSupportActionBar().setTitle("Fruit Detection");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         commentEditText = findViewById(R.id.commentEditText);
         postButton = findViewById(R.id.postButton);
         commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

         commentList = new ArrayList<>();
         commentAdapter = new CommentAdapter(commentList);

         LinearLayoutManager layoutManager = new LinearLayoutManager(this);
         commentsRecyclerView.setLayoutManager(layoutManager);
         commentsRecyclerView.setAdapter(commentAdapter);

         commentsRef = FirebaseDatabase.getInstance().getReference("comments");
         usersRef = FirebaseDatabase.getInstance().getReference("Users");
         currentUser = FirebaseAuth.getInstance().getCurrentUser();

         postButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 postComment();
             }
         });

         loadComments();
     }

     private void postComment() {
         String commentText = commentEditText.getText().toString().trim();

         if (commentText.isEmpty()) {
             Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
             return;
         }

         String userEmail = currentUser.getEmail();
         String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

         usersRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()) {
                     User user = dataSnapshot.getValue(User.class);
                     if (user != null) {
                         String userName = user.getName();
                         Comment comment = new Comment(userName, null, currentTime, commentText); //userEmail remove korte hbe
                         String commentId = commentsRef.push().getKey(); 
                         commentsRef.child(commentId).setValue(comment);
                         commentEditText.setText("");
                         Toast.makeText(CommentsActivity.this, "Comment posted successfully", Toast.LENGTH_SHORT).show();
                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Toast.makeText(CommentsActivity.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
             }
         });
     }

     private void loadComments() {
         commentsRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 commentList.clear();
                 for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                     Comment comment = commentSnapshot.getValue(Comment.class);
                     commentList.add(comment);
                 }
                 commentAdapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Toast.makeText(CommentsActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
             }
         });
     }
 }
