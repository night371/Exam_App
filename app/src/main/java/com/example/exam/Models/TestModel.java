package com.example.exam.Models;

public class TestModel {

    private String id;
    private int topScore;
    private int time;

    public TestModel(String id, int topScore, int time) {
        this.id = id;
        this.topScore = topScore;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
