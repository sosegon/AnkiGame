package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnkiLog extends AppLog{

    // Types of logs
    public static final int GO_TO_GAME = 201;
    public static final int TAKE_QUIZZ = 202;

    // Fields in log
    public int earnedCoins;
    public int revisedCards;
    public int correctAnswers;

    public static AnkiLog logBase() {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String sDate = dateFormat.format(newDate);
        String sTime = timeFormat.format(newDate);

        return new AnkiLog(sDate, sTime);
    }

    public AnkiLog(String date, String time) {
        super(date, time);
    }

    public void setGoToGame(int totalCoins, int earnedCoins) {
        // TODO: AnkiGame, finish implementation
        this.earnedCoins = earnedCoins;
        this.logType = GO_TO_GAME;
    }

    public void setTakeQuizz(int revisedCards, int correctAnswers) {
        this.revisedCards = revisedCards;
        this.correctAnswers = correctAnswers;
        this.logType = TAKE_QUIZZ;
    }

}
