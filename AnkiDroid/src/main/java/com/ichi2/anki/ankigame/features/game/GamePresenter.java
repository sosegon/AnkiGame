package com.ichi2.anki.ankigame.features.game;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.Board;
import com.ichi2.anki.ankigame.data.model.GameLog;
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
        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        mDataManager.getPreferencesHelper().storeCoins(totalCoins - coins);
    }

    public void increaseCoins(int coins) {
        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        mDataManager.getPreferencesHelper().storeCoins(totalCoins + coins);
    }

    public String initUser() {
        return mDataManager.initUser();
    }

    public void updateBestScore(int bestScore) {
        mDataManager.updateBestScore(bestScore);

        // TODO: AnkiGame, Display message when user reaches top of leaderboard.
        // Keep in mind that doing it constantly once the top is reached can be
        // annoying.
    }

    public void log(Board board, int logType) {
        GameLog gameLog = GameLog.logBase();

        switch (logType) {
            case GameLog.GO_TO_ANKI:
                gameLog = logGoToAnki(board);
                break;
            case GameLog.RESTART_GAME:
                gameLog = logRestartGame(board);
                break;
            default:
                break;
        }

        mDataManager.logBehaviour(gameLog);
    }

    public void logUseTrick(Board board, String trickName, boolean trickExecuted) {
        GameLog gameLog = GameLog.logBase();
        String userId = mDataManager.getPreferencesHelper().retrieveUserId();
        gameLog.setUserId(userId);

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();

        gameLog.setUseTrick(
                board,
                totalCoins,
                trickName,
                trickExecuted
        );

        mDataManager.logBehaviour(gameLog);
    }

    private GameLog logGoToAnki(Board board) {
        GameLog gameLog = GameLog.logBase();
        String userId = mDataManager.getPreferencesHelper().retrieveUserId();
        gameLog.setUserId(userId);

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        gameLog.setGoToAnki(
                board,
                totalCoins
        );

        return gameLog;
    }

    private GameLog logRestartGame(Board board) {
        GameLog gameLog = GameLog.logBase();
        String userId = mDataManager.getPreferencesHelper().retrieveUserId();
        gameLog.setUserId(userId);

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        gameLog.setRestartGame(
                board,
                totalCoins
        );

        return gameLog;
    }
}
