package com.freechetwithyounme.chettingapp.Model;

public class HomeModel {

    private String name;
    private String status;
    private String location;
    private String gender;
    private String email;
    private String age;
    private String number;
    private String profession;
    private String bio;
    private String imageuri;

    public HomeModel() {
    }

    public HomeModel(String name, String status, String location, String gender, String email,
                     String age, String number, String profession, String bio, String imageuri) {
        this.name = name;
        this.status = status;
        this.location = location;
        this.gender = gender;
        this.email = email;
        this.age = age;
        this.number = number;
        this.profession = profession;
        this.bio = bio;
        this.imageuri = imageuri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}
