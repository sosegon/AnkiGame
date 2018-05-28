package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AppLog {
    // Types of logs
    public static final int NONE = 0;

    // Keys for fields in log
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_LOG_TYPE = "logType";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_BEST_SCORE = "bestScore";
    public static final String KEY_TOTAL_COINS = "totalCoins";
    public static final String KEY_GAME_MODE = "gameMode";
    public static final String KEY_LEADERBOARD_POSITION = "loaderboardPosition";
    public static final String KEY_CURRENT_SCORE= "currentScore";
    public static final String KEY_USED_TRICKS= "usedTricks";
    public static final String KEY_TRICK_EXECUTED= "trickExecuted";
    public static final String KEY_TRICK_TYPE= "trickType";
    public static final String KEY_BOARD_VALUES= "boardValues";
    public static final String KEY_MOVES= "moves";
    public static final String KEY_SELECTED_GAME_MODE= "selectedGameMode";
    public static final String KEY_EARNED_COINS= "earnedCoins";
    public static final String KEY_REVISED_CARDS= "revisedCards";
    public static final String KEY_CORRECT_ANSWERS= "correctAnswers";

    // Information to be stored in the logs
    public String userId;
    public int logType;
    public String date;
    public String time;

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
}
