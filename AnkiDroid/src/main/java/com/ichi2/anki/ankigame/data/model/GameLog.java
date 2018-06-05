package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameLog extends AppLog {

    // Types of logs
    public static final String TYPE_USE_TRICK = "useTrick";
    public static final String TYPE_RESTART_GAME = "restartGame";
    public static final String TYPE_GO_TO_ANKI = "goToAnki";
    public static final String TYPE_LOST_GAME = "lostGame";
    public static final String TYPE_FAIL_TRICK = "failTrick";
    public static final String TYPE_CHECK_LEADERBOARD = "checkLeaderboard";

    // Name of params
    public static final String PARAM_BEST_SCORE = "bestScore";
    public static final String PARAM_CURRENT_SCORE = "currentScore";
    public static final String PARAM_USED_TRICKS = "usedTricks";
    public static final String PARAM_TRICK_TYPE = "trickType";
    public static final String PARAM_BOARD_VALUES = "boardValues";
    public static final String PARAM_FAIL_TRICK_TYPE = "failTrickType";

    // Params in logEvent
    public int bestScore;
    public int currentScore;
    public String usedTricks;
    public String trickType;
    public String boardValues;
    public String failTrickType;

    public static GameLog logBase(String userId) {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String sDate = dateFormat.format(newDate);
        String sTime = timeFormat.format(newDate);

        GameLog gameLog = new GameLog(sDate, sTime);
        gameLog.setUserId(userId);

        return gameLog;
    }

    public GameLog(String date, String time) {
        super(date, time);
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public String getUsedTricks() {
        return usedTricks;
    }

    public void setUsedTricks(String usedTricks) {
        this.usedTricks = usedTricks;
    }

    public String getTrickType() {
        return trickType;
    }

    public void setTrickType(String trickType) {
        this.trickType = trickType;
    }

    public String getBoardValues() {
        return boardValues;
    }

    public void setBoardValues(String boardValues) {
        this.boardValues = boardValues;
    }

    public String getFailTrickType() {
        return failTrickType;
    }

    public void setFailTrickType(String failTrickType) {
        this.failTrickType = failTrickType;
    }
}
