package com.ichi2.anki.ankigame.features.reviewer;

import android.support.v4.content.ContextCompat;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.AnkiLog;

import javax.inject.Inject;

import static com.ichi2.anki.AbstractFlashcardViewer.EASE_1;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_2;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_3;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_4;

public class ReviewerPresenter extends BasePresenter<ReviewerMvpView> {
    private final DataManager mDataManager;
    private String mDeckInfo;
    private String mDueDeckInfo;
    private boolean mIsFavCard;
    private String mCardInfo;
    private String mCardAnswer;
    // Used to record time between watching a question and answering it
    // Also used to record time between answering a question and assesing it
    private long mElapsedTime;

    @Inject
    public ReviewerPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int increaseCoins(int ease) {
        int currentCoins = 0;

        // TODO: ANKIGAME, Double check the values
        switch (ease) {
            case EASE_1:
                currentCoins += 1;
                break;
            case EASE_2:
                currentCoins += 2;
                break;
            case EASE_3:
                currentCoins += 3;
                break;
            case EASE_4:
                currentCoins += 4;
                break;
        }

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        mDataManager.getPreferencesHelper().storeCoins(totalCoins + currentCoins);

        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public void logSelectDeck() {
        AnkiLog ankiLog = AnkiLog.logBase();
        ankiLog.setLogType(AnkiLog.SELECT_DECK);
        ankiLog.setUserId(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setTotalCoins(mDataManager.getPreferencesHelper().retrieveCoins());
        ankiLog.setDeckInfo(mDeckInfo);
        ankiLog.setDueDeckInfo(mDueDeckInfo);

        mDataManager.logBehaviour(ankiLog);
    }

    public void logDisplayAnswerCard() {
        AnkiLog ankiLog = AnkiLog.logBase();
        ankiLog.setLogType(AnkiLog.DISPLAY_ANSWER_CARD);
        ankiLog.setUserId(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setTotalCoins(mDataManager.getPreferencesHelper().retrieveCoins());
        ankiLog.setCardAnswer(mCardAnswer);
        ankiLog.setCardInfo(mCardInfo);
        ankiLog.setElapsedTime((int)((System.currentTimeMillis() - mElapsedTime)/ 1000));
        ankiLog.setFavCard(mIsFavCard);
        ankiLog.setDeckInfo(mDeckInfo);
        ankiLog.setDueDeckInfo(mDueDeckInfo);

        mDataManager.logBehaviour(ankiLog);
    }

    public void setDeckInfo(String mDeckInfo) {
        this.mDeckInfo = mDeckInfo;
    }

    public void setDueDeckInfo(String mDueDeckInfo) {
        this.mDueDeckInfo = mDueDeckInfo;
    }

    public void setElapsedTime(long mElapsedTime) {
        this.mElapsedTime = mElapsedTime;
    }

    public void setIsFavCard(boolean mIsFavCard) {
        this.mIsFavCard = mIsFavCard;
    }

    public void setCardInfo(String mCardInfo) {
        this.mCardInfo = mCardInfo;
    }

    public void setCardAnswer(String mCardAnswer) {
        this.mCardAnswer = mCardAnswer;
    }
}