package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnkiLog extends AppLog{

    // Types of logs
    public static final int GO_TO_GAME = 201;
    public static final int TAKE_QUIZZ = 202;
    public static final int SELECT_DECK = 203;
    public static final int DISPLAY_ANSWER_CARD = 204;

    // Fields in log
    public String dueDeckInfo;
    public String deckInfo;
    public String cardInfo;
    public String cardAnswer;
    public int elapsedTime;

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

    public void setSelectDeck(String deckInfo, String dueDeckInfo, int totalCoins) {
        this.deckInfo = deckInfo;
        this.dueDeckInfo = dueDeckInfo;
        this.totalCoins = totalCoins;
        this.logType = SELECT_DECK;
    }

    public void setDisplayAnswerCard(String cardInfo, String cardAnswer, int totalCoins, int elapsedTime, String deckInfo, String dueDeckInfo) {
        this.cardInfo = cardInfo;
        this.cardAnswer = cardAnswer;
        this.totalCoins = totalCoins;
        this.elapsedTime = elapsedTime;
        this.deckInfo = deckInfo;
        this.dueDeckInfo = dueDeckInfo;
        this.logType = DISPLAY_ANSWER_CARD;

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
