package com.example.exam.Models;

public class CategoryModel {
    String docId;
    String name;
    int numOfTests;

    public CategoryModel(String docId, String name, int numOfTests) {
        this.docId = docId;
        this.name = name;
        this.numOfTests = numOfTests;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfTests() {
        return numOfTests;
    }

    public void setNumOfTests(int numOfTests) {
        this.numOfTests = numOfTests;
    }



}
