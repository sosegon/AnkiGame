package com.ichi2.anki.ankigame.features.deckpicker;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class DeckPickerPresenter extends BasePresenter<DeckPickerMvpView> {
    private final DataManager mDataManager;

    @Inject
    public DeckPickerPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
    }

    public String getShareUrl() {
        return mDataManager.getShareUrl();
    }

    public void logGoToGame() {
        AnkiLog ankiLog = AnkiLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setLogType(AnkiLog.TYPE_GO_TO_GAME);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setEarnedPoints(getEarnedPoints());
        ankiLog.setEarnedCoins(getEarnedCoins());
        mDataManager.logBehaviour(ankiLog);
    }

    public String initUser() {
        return mDataManager.initUser();
    }

    private int getEarnedCoins() {
        return mDataManager.getPreferencesHelper().retrieveEarnedCoins();
    }

    private int getEarnedPoints() {
        return mDataManager.getPreferencesHelper().retrieveEarnedPoints();
    }

}
