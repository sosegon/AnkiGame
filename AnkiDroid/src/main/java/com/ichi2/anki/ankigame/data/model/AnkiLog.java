package com.ichi2.anki.ankigame.data.model;

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
    public int[] usedTricks;
    public int trickType;
    public int[][] boardState;
    public int moves;
    public int selectedGameMode;
    public int earnedCoins;
    public int revisedCards;
    public int correctAnswers;

    public AnkiLog(String userId, String date, String time) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.logType = NONE;
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
        this.usedTricks = usedTricks;
        this.logType = END_GAME;
    }

    public void setUseTrick(int bestScore, int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int trickType, int[][] boardState, int moves) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        this.trickType = trickType;
        this.boardState = boardState;
        this.moves = moves;
        this.logType = USE_TRICK;
    }

    public void setSelectGameMode(int bestScore, int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int[][] boardState, int selectedGameMode) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        this.boardState = boardState;
        this.selectedGameMode = selectedGameMode;
        this.logType = SELECT_GAME_MODE;
    }

    public void setCheckLeaderboard(int bestScore, int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int[][] boardState) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        this.boardState = boardState;
        this.logType = CHECK_LEADERBOARD;
    }

    public void setRestartGame(int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int usedCoins, int[] usedTricks, int[][] boardState) {
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        this.usedCoins = usedCoins;
        this.usedTricks = usedTricks;
        this.boardState = boardState;
        this.logType = RESTART_GAME;
    }

    public void setGoToAnki(int bestScore, int totalCoins, int gameMode, int leaderboardPosition, int currentScore, int usedCoins, int[] usedTricks, int[][] boardState) {
        this.bestScore = bestScore;
        this.totalCoins = totalCoins;
        this.gameMode = gameMode;
        this.leaderboardPosition = leaderboardPosition;
        this.currentScore = currentScore;
        this.usedCoins = usedCoins;
        this.usedTricks = usedTricks;
        this.boardState = boardState;
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
}
