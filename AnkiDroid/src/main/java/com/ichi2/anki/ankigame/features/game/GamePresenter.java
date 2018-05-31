package com.ichi2.anki.ankigame.features.game;

import android.webkit.JavascriptInterface;

import com.ichi2.anki.BuildConfig;
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

    public int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
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

    public void log(Board board, String logType) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());

        if(logType.contentEquals(GameLog.TYPE_GO_TO_ANKI)) {
            gameLog = logGoToAnki(board);
        } else if (logType.contentEquals(GameLog.TYPE_RESTART_GAME)) {
            gameLog = logRestartGame(board);
        }

        mDataManager.logBehaviour(gameLog);
    }

    @JavascriptInterface
    public boolean hasPointsForTrick(int requiredPoints) {
        int points =  getPoints();

        if(BuildConfig.FLAVOR.contentEquals("independent")) {
            return true;
        }

        if(requiredPoints > 0 && points >= requiredPoints) {
            return true;
        }

        return false;
    }

    @JavascriptInterface
    public void noPointsForTrick(String trickName, int requiredPoints, String jsonString) {
        int points =  getPoints();
        Board board = Board.parseJSON(jsonString);

        getMvpView().postRunnable(new Runnable(){
            @Override
            public void run () {
                getMvpView().showBlockedTrickToast(requiredPoints - points);
                logUseTrick(board, trickName, false);
            }
        });
    }

    @JavascriptInterface
    public boolean hasCoinsForTrick(int requiredCoins) {
        int coins =  getCoins();

        if(BuildConfig.FLAVOR.contentEquals("independent")) {
            return true;
        }

        if(requiredCoins > 0 && coins >= requiredCoins) {
            return true;
        }

        return false;
    }

    @JavascriptInterface
    public void noCoinsForTrick(String trickName, int requiredCoins, String jsonString) {
        int coins =  getCoins();
        Board board = Board.parseJSON(jsonString);

        getMvpView().postRunnable(new Runnable(){
            @Override
            public void run () {
                getMvpView().showNoCoinsToast(requiredCoins - coins);
                logUseTrick(board, trickName, false);
            }
        });
    }

    @JavascriptInterface
    public void unableTrick(String trickName, String jsonString) {
        Board board = Board.parseJSON(jsonString);
        getMvpView().postRunnable(new Runnable(){
            @Override
            public void run () {
                getMvpView().showUnableToDoTrickToast(trickName);
                logUseTrick(board, trickName, false);
            }
        });
    }

    @JavascriptInterface
    public void doTrick(String trickName, int requiredCoins, String jsonString) {
        Board board = Board.parseJSON(jsonString);

        reduceCoins(requiredCoins);
        // WebView has its own threads, therefore, updates in the UI has to be done accordingly
        // The following line has to be executed in a handler created in the activity
        // mGameView.updateLblGameCoins(mGamePresenter.getCoins());
        getMvpView().postRunnable(new Runnable(){
            @Override
            public void run () {
                getMvpView().updateLblGameCoins(getCoins());
                logUseTrick(board, trickName, true);
            }
        });
    }

    @JavascriptInterface
    public void getBoardState(String jsonString, String logType) {
        getMvpView().postRunnable(new Runnable(){
            @Override
            public void run () {
                Board board =  Board.parseJSON(jsonString);
                log(board, logType);
            }
        });
    }

    @JavascriptInterface
    public void updateBestScore(int bestScore) {
        getMvpView().postRunnable(new Runnable(){
            @Override
            public void run () {
                mDataManager.updateBestScore(bestScore);

                // TODO: AnkiGame, Display message when user reaches top of leaderboard.
                // Keep in mind that doing it constantly once the top is reached can be
                // annoying.
            }
        });
    }

    @JavascriptInterface
    public int getAnkiPoints() {
        return getPoints();
    }

    private GameLog logGoToAnki(Board board) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        gameLog.setLogType(GameLog.TYPE_GO_TO_ANKI);
        gameLog.setBestScore(board.getBestScore());
        gameLog.setCurrentScore(board.getScore());
        gameLog.setUsedTricks(board.getUsedTricksAsString());
        gameLog.setBoardValues(board.getBoardValuesAsString());
        gameLog.setTotalCoins(mDataManager.getPreferencesHelper().retrieveCoins());
        // TODO: AnkiGame Add leaderboard position

        return gameLog;
    }

    private GameLog logRestartGame(Board board) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        gameLog.setLogType(GameLog.TYPE_RESTART_GAME);
        gameLog.setBestScore(board.getBestScore());
        gameLog.setCurrentScore(board.getScore());
        gameLog.setUsedTricks(board.getUsedTricksAsString());
        gameLog.setBoardValues(board.getBoardValuesAsString());
        gameLog.setTotalCoins(mDataManager.getPreferencesHelper().retrieveCoins());
        // TODO: AnkiGame Add leaderboard position

        return gameLog;
    }

    private void logUseTrick(Board board, String trickName, boolean trickExecuted) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        gameLog.setLogType(GameLog.TYPE_USE_TRICK);
        gameLog.setBestScore(board.getBestScore());
        gameLog.setCurrentScore(board.getScore());
        gameLog.setUsedTricks(board.getUsedTricksAsString());
        gameLog.setBoardValues(board.getBoardValuesAsString());
        gameLog.setTotalCoins(mDataManager.getPreferencesHelper().retrieveCoins());
        gameLog.setTrickType(trickName);
        gameLog.setTrickExecuted(trickExecuted);

        mDataManager.logBehaviour(gameLog);
    }

}
