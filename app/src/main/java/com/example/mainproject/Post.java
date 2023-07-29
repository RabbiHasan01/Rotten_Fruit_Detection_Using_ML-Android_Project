package com.example.mainproject;

import java.util.Date;
import java.util.List;

public class Post {
    private String imageUrl;
    private String caption;
    private String postId;
    private long timestamp;

    private boolean currentUserLiked;
    private int likeCount; // New property for storing the number of likes

    private List<Comment> commentsList; // New property for storing the comments

    public Post() {
        // Default constructor required for Firebase
    }

    public Post(String imageUrl, String caption, String postId) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.caption = caption;
        this.timestamp = new Date().getTime(); // Set the current timestamp when the post is created
        this.likeCount = 0; // Initialize the like count to 0 when the post is created
    }

    public String getCaption() {
        return caption;
    }

    public String getPostId() {
        return postId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isCurrentUserLiked() {
        return currentUserLiked;
    }

    public void setCurrentUserLiked(boolean currentUserLiked) {
        this.currentUserLiked = currentUserLiked;
    }

    public List<Comment> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comment> commentsList) {
        this.commentsList = commentsList;
    }
}
