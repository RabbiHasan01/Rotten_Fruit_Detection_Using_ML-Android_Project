package com.example.mainproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

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
