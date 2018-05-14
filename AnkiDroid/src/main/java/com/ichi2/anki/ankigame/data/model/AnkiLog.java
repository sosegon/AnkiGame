package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnkiLog {

    // Types of logs
    public static final int NONE = 0;
    public static final int START_GAME = 1;
    public static final int END_GAME = 2;
    public static final int USE_TRICK = 3;
    public static final int SELECT_GAME_MODE = 4;
    public static final int CHECK_LEADERBOARD = 5;
    public static final int RESTART_GAME = 6;
    public static final int GO_TO_ANKI = 7;
    public static final int GO_TO_GAME = 8;
    public static final int TAKE_QUIZZ = 9;

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
    public static final String KEY_USED_COINS= "usedCoins";
    public static final String KEY_USED_TRICKS= "usedTricks";
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
    public int bestScore;
    public int totalCoins;
    public int gameMode;
    public int leaderboardPosition;
    public int currentScore;
    public int usedCoins;
    public String usedTricks;
    public int trickType;
    public String boardValues;
    public int moves;
    public int selectedGameMode;
    public int earnedCoins;
    public int revisedCards;
    public int correctAnswers;

    public AnkiLog(String date, String time) {
        this.date = date;
        this.time = time;
        this.logType = NONE;
    }

    public static AnkiLog logBase() {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String sDate = dateFormat.format(newDate);
        String sTime = timeFormat.format(newDate);

        return new AnkiLog(sDate, sTime);
    }

    public void setStartGame(int bestScore, int totalCoins, int gameMode, int leaderboardPosition) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.logType = START_GAME;
    }

    public void setEndGame(int bestScore, int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int usedCoins, int[] usedTricks) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        this.usedCoins = usedCoins;
        //this.usedTricks = usedTricks;
        this.logType = END_GAME;
    }

    public void setUseTrick(int bestScore, int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int trickType, int[][] boardState, int moves) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        this.trickType = trickType;
        //this.boardValues = boardValues;
        this.moves = moves;
        this.logType = USE_TRICK;
    }

    public void setSelectGameMode(int bestScore, int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int[][] boardState, int selectedGameMode) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        //this.boardValues = boardValues;
        this.selectedGameMode = selectedGameMode;
        this.logType = SELECT_GAME_MODE;
    }

    public void setCheckLeaderboard(int bestScore, int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int[][] boardState) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        //this.boardValues = boardValues;
        this.logType = CHECK_LEADERBOARD;
    }

    public void setRestartGame(int bestScore, int totalCoins, int currentScore, String usedTricks, String boardValues) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        //this.gameMode = gameMode;
        //this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        //this.usedCoins = usedCoins;
        this.usedTricks = usedTricks;
        this.boardValues = boardValues;
        this.logType = RESTART_GAME;
    }

    // TODO: AnkiGame, Check the other commented fields
    public void setGoToAnki(int bestScore, int totalCoins, int currentScore, String usedTricks, String boardValues) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        //this.gameMode = gameMode;
        //this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        //this.usedCoins = usedCoins;
        this.usedTricks = usedTricks;
        this.boardValues = boardValues;
        this.logType = GO_TO_ANKI;
    }

    public void setGoToGame(int totalCoins, int earnedCoins) {
        this.totalCoins = totalCoins;
        this.earnedCoins = earnedCoins;
        this.logType = GO_TO_GAME;
    }

    public void setTakeQuizz(int revisedCards, int correctAnswers) {
        this.revisedCards = revisedCards;
        this.correctAnswers = correctAnswers;
        this.logType = TAKE_QUIZZ;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
