package com.example.mainproject;

public class User {
    private String name;
    private String username;
    private String email;
    private String age;
    private String gender;
    //private String imageUrl;

    // Default constructor (required for Firebase)
    public User() {
    }

    // Constructor
    public User(String name, String username, String email, String age, String gender) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
        //this.imageUrl = imageUrl;
    }

    // Getters and setters for the fields

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /*public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }*/
}
