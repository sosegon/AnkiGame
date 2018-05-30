package com.ichi2.anki.ankigame.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnkiLog extends AppLog{

    // Types of logs
    public static final String GO_TO_GAME = "goToGame";
    public static final String SELECT_DECK = "selectDeck";
    public static final String DISPLAY_ANSWER_CARD = "displayAnswerCard";
    public static final String ASSESS_CARD = "assessCard";

    // Fields in log
    public String dueDeckInfo;
    public String deckInfo;
    public String cardInfo;
    public String cardAnswer;
    public int elapsedTime;
    public boolean isFavCard;
    public int coinsInCard;
    public int cardEase;

    public static AnkiLog logBase(String userId) {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String sDate = dateFormat.format(newDate);
        String sTime = timeFormat.format(newDate);

        AnkiLog ankiLog = new AnkiLog(sDate, sTime);
        ankiLog.setUserId(userId);

        return ankiLog;
    }

    public AnkiLog(String date, String time) {
        super(date, time);
    }

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

    public int getCoinsInCard() {
        return coinsInCard;
    }

    public void setCoinsInCard(int coinsInCard) {
        this.coinsInCard = coinsInCard;
    }

    public int getCardEase() {
        return cardEase;
    }

    public void setCardEase(int cardEase) {
        this.cardEase = cardEase;
    }
}
