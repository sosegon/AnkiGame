package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameLog extends AppLog {

    // Types of logs
    public static final int START_GAME = 101;
    public static final int END_GAME = 102;
    public static final int USE_TRICK = 103;
    public static final int SELECT_GAME_MODE = 104;
    public static final int CHECK_LEADERBOARD = 105;
    public static final int RESTART_GAME = 106;
    public static final int GO_TO_ANKI = 107;

    // Fields in log
    public int bestScore;
    public int gameMode;
    public int leaderboardPosition;
    public int currentScore;
    public String usedTricks;
    public String trickType;
    public boolean trickExecuted;
    public String boardValues;
    public int moves;
    public int selectedGameMode;

    public static GameLog logBase() {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String sDate = dateFormat.format(newDate);
        String sTime = timeFormat.format(newDate);

        return new GameLog(sDate, sTime);
    }

    public GameLog(String date, String time) {
        super(date, time);
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
        //this.usedTricks = usedTricks;
        this.logType = END_GAME;
    }

    // TODO: AnkiGame, Implement the commented fields
    public void setUseTrick(Board board, int totalCoins, String trickType, boolean trickExecuted) {
        this.bestScore = board.getBestScore();
        this.currentScore = board.getScore();
        this.usedTricks = board.getUsedTricksAsString();
        this.boardValues = board.getBoardValuesAsString();
        this.totalCoins = totalCoins;
        // this.gameMode = gameMode;
        // this.leaderboardPosition = leaderboardPosition;
        this.trickType = trickType;
        //this.boardValues = boardValues;
        //this.moves = moves;
        this.trickExecuted = trickExecuted;

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

    // TODO: AnkiGame, Implement the commented fields
    public void setRestartGame(Board board, int totalCoins) {
        this.bestScore = board.getBestScore();
        this.currentScore = board.getScore();
        this.usedTricks = board.getUsedTricksAsString();
        this.boardValues = board.getBoardValuesAsString();
        this.totalCoins = totalCoins;
        //this.gameMode = gameMode;
        //this.leaderboardPosition = leaderboardPosition;

        this.logType = RESTART_GAME;
    }

    // TODO: AnkiGame, Implement the commented fields
    public void setGoToAnki(Board board, int totalCoins) {
        this.bestScore = board.getBestScore();
        this.currentScore = board.getScore();
        this.usedTricks = board.getUsedTricksAsString();
        this.boardValues = board.getBoardValuesAsString();
        this.totalCoins = totalCoins;
        //this.gameMode = gameMode;
        //this.leaderboardPosition = leaderboardPosition;

        this.logType = GO_TO_ANKI;
    }
}
