package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameLog extends AppLog {

    // Types of logs
    public static final String USE_TRICK = "useTrick";
    public static final String RESTART_GAME = "restartGame";
    public static final String GO_TO_ANKI = "goToAnki";

    // Fields in log
    public int bestScore;
    public int currentScore;
    public String usedTricks;
    public String trickType;
    public boolean trickExecuted;
    public String boardValues;

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

    public boolean isTrickExecuted() {
        return trickExecuted;
    }

    public void setTrickExecuted(boolean trickExecuted) {
        this.trickExecuted = trickExecuted;
    }

    public String getBoardValues() {
        return boardValues;
    }

    public void setBoardValues(String boardValues) {
        this.boardValues = boardValues;
    }
}
