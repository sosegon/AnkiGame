package com.ichi2.game;

import com.ichi2.game.base.BasePresenter;
import com.ichi2.game.data.DataManager;

import javax.inject.Inject;

public class GamePresenter extends BasePresenter<GameMvpView> {
    private final DataManager mDataManager;

    @Inject
    public GamePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void addMessage(String message) {

    }
}
