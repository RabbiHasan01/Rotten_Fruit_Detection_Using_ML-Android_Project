package com.example.mainproject;

public class Users {
    private String name;
    private String username;
    private String email;
    private String age;
    private String gender;
    private String profileImageURL;

    public Users() {
        // Default constructor required for Firebase
    }

    public Users(User user, String profileImageURL) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.profileImageURL = profileImageURL;
    }

    public  String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }


}



