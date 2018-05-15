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

    public void log(Board board, int logType) {
        AnkiLog ankiLog = AnkiLog.logBase();

        switch (logType) {
            case AnkiLog.GO_TO_ANKI:
                ankiLog = logGoToAnki(board);
                break;
            case AnkiLog.RESTART_GAME:
                ankiLog = logRestartGame(board);
                break;
            default:
                break;
        }

        mDataManager.logBehaviour(ankiLog);
    }

    public void logUseTrick(Board board, String trickName, boolean trickExecuted) {
        AnkiLog ankiLog = AnkiLog.logBase();
        String userId = mDataManager.getPreferencesHelper().retrieveUserId();
        ankiLog.setUserId(userId);

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();

        ankiLog.setUseTrick(
                board,
                totalCoins,
                trickName,
                trickExecuted
        );

        mDataManager.logBehaviour(ankiLog);
    }

    private AnkiLog logGoToAnki(Board board) {
        AnkiLog ankiLog = AnkiLog.logBase();
        String userId = mDataManager.getPreferencesHelper().retrieveUserId();
        ankiLog.setUserId(userId);

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        ankiLog.setGoToAnki(
                board,
                totalCoins
        );

        return ankiLog;
    }

    private AnkiLog logRestartGame(Board board) {
        AnkiLog ankiLog = AnkiLog.logBase();
        String userId = mDataManager.getPreferencesHelper().retrieveUserId();
        ankiLog.setUserId(userId);

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        ankiLog.setRestartGame(
                board,
                totalCoins
        );

        return ankiLog;
    }
}
