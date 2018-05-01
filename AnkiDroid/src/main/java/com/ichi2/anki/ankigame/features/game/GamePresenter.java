package com.ichi2.anki.ankigame.features.game;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class GamePresenter extends BasePresenter<GameMvpView> {
    private final DataManager mDataManager;

    @Inject
    public GamePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public void reduceCoins(int coins) {
        mDataManager.getPreferencesHelper().updateCoins(-coins);
    }

    public void increaseCoins(int coins) {
        mDataManager.getPreferencesHelper().updateCoins(coins);
    }
}
