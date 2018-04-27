package com.ichi2.game;

import com.ichi2.game.base.BasePresenter;
import com.ichi2.game.data.DataManager;
import com.ichi2.game.injection.ConfigPersistent;

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
}
