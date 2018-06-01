package com.ichi2.anki.ankigame.data.model;

public abstract class AppLog {
    // Types of logs
    public static final String TYPE_NONE = "none";

    // Name of params
    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_LOG_TYPE = "logType";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_TOTAL_COINS = "totalCoins";
    public static final String PARAM_TOTAL_POINTS = "totalPoints";

    // Params in logEvent
    public String userId;
    public String logType;
    public String date;
    public String time;
    public int totalCoins;
    public int totalPoints;

    public AppLog(String date, String time) {
        this.date = date;
        this.time = time;
        this.logType = TYPE_NONE;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
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

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}
