package com.ichi2.anki.ankigame.features.game;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.data.model.Board;
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

    public String initUser() {
        return mDataManager.initUser();
    }

    public void logGoToAnki(Board board) {
        AnkiLog ankiLog = AnkiLog.logBase();

        String userId = mDataManager.getPreferencesHelper().retrieveUserId();
        ankiLog.setUserId(userId);

        int bestScore = board.getBestScore();
        int currentScore = board.getScore();
        String usedTricks = board.getUsedTricksAsString();
        String boardValues = board.getBoardValuesAsString();
        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        ankiLog.setGoToAnki(
            bestScore,
            totalCoins,
            currentScore,
            usedTricks,
            boardValues
        );

        mDataManager.logBehaviour(ankiLog);
    }
}
