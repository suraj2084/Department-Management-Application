package com.example.android.admincollegeapp;

class NoticeData {
    String title,Image,Date,time,Key;
    public NoticeData(){

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return Image;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return time;
    }

    public String getKey() {
        return Key;
    }

    public NoticeData(String title, String image, String date, String time, String key) {
        this.title = title;
        Image = image;
        Date = date;
        this.time = time;
        Key = key;
    }
}
