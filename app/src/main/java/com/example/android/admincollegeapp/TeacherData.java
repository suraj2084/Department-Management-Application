package com.example.android.admincollegeapp;

public class TeacherData {
    private String Name,email,phone,image,key;

    public TeacherData() {

    }

    public TeacherData(String name, String email, String phone, String image, String key) {
        Name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.key = key;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
