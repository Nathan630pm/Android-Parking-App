package com.nathan630pm.nk_final_project.models;

public class User {
    String email;
    String name;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
    }

    public User() {
        this.email = "";
        this.name = "";
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

}
