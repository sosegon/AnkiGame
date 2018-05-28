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
    public boolean isFavCard;

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

   /* public void setSelectDeck(String deckInfo, String dueDeckInfo, int totalCoins) {
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

    }*/

    public String getDueDeckInfo() {
        return dueDeckInfo;
    }

    public void setDueDeckInfo(String dueDeckInfo) {
        this.dueDeckInfo = dueDeckInfo;
    }

    public String getDeckInfo() {
        return deckInfo;
    }

    public void setDeckInfo(String deckInfo) {
        this.deckInfo = deckInfo;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getCardAnswer() {
        return cardAnswer;
    }

    public void setCardAnswer(String cardAnswer) {
        this.cardAnswer = cardAnswer;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public boolean isFavCard() {
        return isFavCard;
    }

    public void setFavCard(boolean favCard) {
        isFavCard = favCard;
    }
}
