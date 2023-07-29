package com.example.mainproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Post> postList;
    private FirebaseUser currentUser;

    private DatabaseReference commentsRef;
    private DatabaseReference usersRef;


    // Create a HashMap to keep track of LikeHandlers for each post.
    private HashMap<String, LikeHandler> likeHandlers = new HashMap<>();

    public ImageAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = postList.get(position);

        //holder.textViewUser.setText(users.getName());

        // Fetch user's name for the given post
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        usersRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String userName = user.getName();
                        holder.textViewUser.setText(userName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(ImageAdapter.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
            }
        });

        //holder.textViewUser.setText(post.getUserName());
        String timeFormatted = getTimeFormatted(post.getTimestamp());
        holder.textViewTime.setText(timeFormatted);

        holder.textViewCaption.setText(post.getCaption());
        Picasso.get().load(post.getImageUrl()).into(holder.imageView);

        // Set OnClickListener for textViewComment
        holder.textViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event here
                Post post = postList.get(position);
                String postId = post.getPostId();

                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("postId", postId);
                context.startActivity(intent);

                //Toast.makeText(context, "Comment clicked for post. " , Toast.LENGTH_SHORT).show();+
            }
        });

        // Initialize and set up the CommentAdapter for the inner RecyclerView
        CommentAdapter commentAdapter = new CommentAdapter(post.getCommentsList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.commentsRecyclerView.setLayoutManager(layoutManager);
        holder.commentsRecyclerView.setAdapter(commentAdapter);


    }

    private void updateLikeTextView(ImageViewHolder holder, LikeHandler likeHandler) {
        holder.textViewLike.setText(String.valueOf(likeHandler.getLikeCount()));
        if (likeHandler.isLiked()) {
            // Set the liked state (you can use different drawables for liked and unliked states)
            holder.textViewLike.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.liked_btn, 0, 0, 0);
        } else {
            holder.textViewLike.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.dislike_btn, 0, 0, 0);
        }
    }
    //for date formation.....
    private String getTimeFormatted(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /*
    // Inner RecyclerView.Adapter for comments
    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

        private List<Comment> commentList;

        public CommentAdapter(List<Comment> commentList) {
            this.commentList = commentList;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = commentList.get(position);
            holder.bind(comment);
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder {

            private TextView userEmailTextView, userNameTextView;
            private TextView timeTextView;
            private TextView commentTextView;

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
                userNameTextView = itemView.findViewById(R.id.userNameTextView);
                timeTextView = itemView.findViewById(R.id.timeTextView);
                commentTextView = itemView.findViewById(R.id.commentTextView);
            }

            public void bind(Comment comment) {
                userEmailTextView.setText(comment.getUserEmail());
                userNameTextView.setText(comment.getUserName());
                timeTextView.setText(comment.getTime());
                commentTextView.setText(comment.getCommentText());
            }
        }
    }

    /*
    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
        private List<Comment> commentList;

        public CommentAdapter(List<Comment> commentList) {
            this.commentList = commentList;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = commentList.get(position);

            // Display the comment text and other information
            holder.textViewCommentText.setText(comment.getCommentText());
            // ... (display other comment information as needed)
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewCommentText; // Add oth+++++er comment views here if needed

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewCommentText = itemView.findViewById(R.id.textViewCommentText); // Initialize comment views
                // ... (initialize other comment views if needed)
            }
        }
    }*/



    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUser, textViewTime, textViewCaption, textViewLike, textViewComment;
        public ImageView imageView;
        public RecyclerView commentsRecyclerView; // Add this view to hold the comments RecyclerView

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imageView = itemView.findViewById(R.id.imageView);
            textViewCaption = itemView.findViewById(R.id.textViewCaption);
            textViewLike = itemView.findViewById(R.id.textViewLike);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            commentsRecyclerView = itemView.findViewById(R.id.commentsRecyclerView); // Initialize commentsRecyclerView
        }
    }
}


