package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AppLog {
    // Types of logs
    public static final int NONE = 0;

    // Information to be stored in the logs
    public String userId;
    public int logType;
    public String date;
    public String time;
    public int totalCoins;

    public AppLog(String date, String time) {
        this.date = date;
        this.time = time;
        this.logType = NONE;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
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

    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }
}
