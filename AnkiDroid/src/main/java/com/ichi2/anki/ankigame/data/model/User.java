package com.ichi2.anki.ankigame.data.model;

public class User {

    public static final String PARAM_BEST_SCORE = "bestScore";
    public static final String PARAM_NICK_NAME = "nickName";
    public static final String PARAM_POINTS = "points";

    private int bestScore;
    private String nickName;
    private int points;

    public User() {
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
