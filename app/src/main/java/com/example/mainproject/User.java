package com.example.mainproject;

public class User {
    private String name;
    private String username;
    private String email;

    private String birthdate;
    private String division;
    private String district;
    private String phone;
    private String gender;
    //private String imageUrl;

    // Default constructor (required for Firebase)
    public User() {
    }

    // Constructor
    public User(String name, String username, String email, String birthdate, String division,String district, String phone, String gender) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.birthdate=birthdate;
        this.division=division;
        this.district=district;
        this.phone = phone;
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

    public String getBirthdate() {return birthdate;}

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getDivision() {return division;}

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {return district;}

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
