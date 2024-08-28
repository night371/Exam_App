package com.example.exam.Models;

public class CategoryModelAdmin {
    String name;
    String id;
    String no_of_test;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo_of_test() {
        return no_of_test;
    }

    public void setNo_of_test(String no_of_test) {
        this.no_of_test = no_of_test;
    }

    public CategoryModelAdmin(String name, String id, String no_of_test) {
        this.name = name;
        this.id = id;
        this.no_of_test = no_of_test;
    }

    public CategoryModelAdmin() {

    }
    public CategoryModelAdmin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
