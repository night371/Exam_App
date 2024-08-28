package com.example.exam.Models;

public class RankModel {
    private String name;
    private int rank;
    private int score;

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public RankModel(int rank, int score, String name) {
        this.rank = rank;
        this.score = score;
        this.name=name;
    }
}
