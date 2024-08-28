package com.example.exam.Models;

public class ProfileModel {
    private String name;
    private String email;
    private String phoneNo;
    private int bookMarkCount;
    public ProfileModel(String name, String email, String phoneNo,int bookMarkCount) {
        this.name = name;
        this.email = email;
        this.phoneNo= phoneNo;
        this.bookMarkCount= bookMarkCount;
    }

    public int getBookMarkCount() {
        return bookMarkCount;
    }

    public void setBookMarkCount(int bookMarkCount) {
        this.bookMarkCount = bookMarkCount;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
