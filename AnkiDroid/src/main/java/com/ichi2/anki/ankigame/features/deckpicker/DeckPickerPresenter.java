package com.ichi2.anki.ankigame.features.deckpicker;

import com.ichi2.anki.AnkiActivity;
import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.features.game.GameMvpView;

import javax.inject.Inject;

public class DeckPickerPresenter extends BasePresenter<DeckPickerMvpView> {
    private final DataManager mDataManager;

    @Inject
    public DeckPickerPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public void logSelectDeck(String deckName, String cardNumbers) {
        AnkiLog ankiLog = AnkiLog.logBase();
        String userId = mDataManager.getPreferencesHelper().retrieveUserId();
        ankiLog.setUserId(userId);

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        ankiLog.setSelectDeck(deckName, cardNumbers, totalCoins);
        mDataManager.getFirebaseHelper().storeLog(ankiLog);
    }
}
