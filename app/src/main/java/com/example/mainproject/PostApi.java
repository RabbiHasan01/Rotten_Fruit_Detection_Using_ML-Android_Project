package com.example.mainproject;

import com.google.gson.annotations.SerializedName;


public class PostApi {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    // Add more fields as needed (e.g., content, author, etc.)
    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getters and Setters for the 'id' field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and Setters for the 'title' field
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Add more getters and setters for other fields as needed
}

