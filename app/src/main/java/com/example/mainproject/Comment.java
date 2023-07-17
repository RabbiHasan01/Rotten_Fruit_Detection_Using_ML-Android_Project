package com.example.mainproject;


public class Comment {
    private String userName;
    private String userEmail;
    private String time;
    private String commentText;

    public Comment() {
        // Empty constructor needed for Firebase
    }

    public Comment(String userName, String userEmail, String time, String commentText) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.time = time;
        this.commentText = commentText;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

    public String getCommentText() {
        return commentText;
    }
}
