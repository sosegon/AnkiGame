package com.ichi2.anki.ankigame.data.model;

public class User {

    public static final String PARAM_BEST_SCORE = "bestScore";
    public static final String PARAM_NICK_NAME = "nickName";
    public static final String PARAM_POINTS = "points";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_ANKIMAL_INDEX = "ankimalIndex";
    public static final String PARAM_COLORED_ANKIMAL = "coloredAnkimal";

    private int bestScore;
    private String nickName;
    private int points;
    public String date;
    public String time;
    public int ankimalIndex;
    public boolean coloredAnkimal;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAnkimalIndex() {
        return ankimalIndex;
    }

    public void setAnkimalIndex(int ankimalIndex) {
        this.ankimalIndex = ankimalIndex;
    }

    public boolean isColoredAnkimal() {
        return coloredAnkimal;
    }

    public void setColoredAnkimal(boolean coloredAnkimal) {
        this.coloredAnkimal = coloredAnkimal;
    }
}
