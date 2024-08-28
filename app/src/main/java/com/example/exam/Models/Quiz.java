package com.example.exam.Models;

public class Quiz {
    public String cat_id;
    public String name;
    public int no_of_tests;

    public Quiz() {
    }

    public Quiz(String cat_id, String name, int no_of_tests) {
        this.cat_id = cat_id;
        this.name = name;
        this.no_of_tests = no_of_tests;
    }
// ... (Constructors and other methods) ...

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo_of_tests() {
        return no_of_tests;
    }

    public void setNo_of_tests(int no_of_tests) {
        this.no_of_tests = no_of_tests;
    }
}