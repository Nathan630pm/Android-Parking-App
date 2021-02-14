package com.nathan630pm.nk_final_project.models;

public class User {
    String email;
    String name;
    String contactNumber;
    String carPlateNumber;

    public User(String email, String name, String contactNumber, String carPlateNumber) {
        this.email = email;
        this.name = name;
        this.contactNumber = contactNumber;
        this.carPlateNumber = carPlateNumber;
    }

    public User() {
        this.email = "";
        this.name = "";
        this.contactNumber = "";
        this.carPlateNumber = "";
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }
}
