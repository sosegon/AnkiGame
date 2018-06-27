package com.ichi2.anki.ankigame.features.game;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.webkit.JavascriptInterface;

import com.ichi2.anki.BuildConfig;
import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.Board;
import com.ichi2.anki.ankigame.data.model.GameLog;
import com.ichi2.anki.ankigame.data.model.Trick;
import com.ichi2.anki.ankigame.injection.ApplicationContext;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;
import com.ichi2.anki.ankigame.util.AnkimalsUtils;

import javax.inject.Inject;

@ConfigPersistent
public class GamePresenter extends BasePresenter<GameMvpView> {

    private final DataManager mDataManager;
    private Context mContext;

    @Inject
    public GamePresenter(@ApplicationContext Context context, DataManager dataManager) {
        this.mContext = context;
        mDataManager = dataManager;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
    }

    public int countFreeAnkimals() {
        return AnkimalsUtils.countFreeAnkimals(mContext, getPoints());
    }

    public Drawable getPlayerDrawableAnkimal() {
        return AnkimalsUtils.getPlayerDrawableAnkimal(mContext, mDataManager);
    }

    public String getNickName() {
        return mDataManager.getPreferencesHelper().retrieveNickName();
    }

    public void resetEarnedCoins() {
        mDataManager.getPreferencesHelper().storeEarnedCoins(0);
    }

    public void resetEarnedPoints() {
        mDataManager.getPreferencesHelper().storeEarnedPoints(0);
    }

    public void reduceCoins(int coins) {
        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        mDataManager.getPreferencesHelper().storeCoins(totalCoins - coins);
    }

    public void increaseCoins(int coins) {
        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        mDataManager.getPreferencesHelper().storeCoins(totalCoins + coins);
    }

    public String getShareUrl() {
        return mDataManager.getShareUrl();
    }

    public void log(Board board, String logType) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());

        if(logType.contentEquals(GameLog.TYPE_GO_TO_ANKI)) {
            gameLog = logGoToAnki(board);
        } else if (logType.contentEquals(GameLog.TYPE_RESTART_GAME)) {
            gameLog = logRestartGame(board);
        } else if (logType.contentEquals(GameLog.TYPE_LOST_GAME)) {
            gameLog = logLostGame(board);
        } else if (logType.contentEquals(GameLog.TYPE_WON_GAME)) {
            gameLog = logWonGame(board);
        } else {
            // Valid for GameLog.TYPE_NONE
            return;
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
                getMvpView().showInsufficientPointsMessage(requiredPoints - points);
                logFailTrick(board, trickName, Trick.TRICK_FAIL_BLOCKED);
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
                getMvpView().showInsufficientCoinsMessage(requiredCoins - coins);
                logFailTrick(board, trickName, Trick.TRICK_FAIL_DISABLED);
            }
        });
    }

    @JavascriptInterface
    public void unableTrick(String trickName, String jsonString) {
        Board board = Board.parseJSON(jsonString);
        getMvpView().postRunnable(new Runnable(){
            @Override
            public void run () {
                getMvpView().showUnableToDoTrickMessage(trickName);
                logFailTrick(board, trickName, Trick.TRICK_FAIL_NOT_PERMITTED);
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
                logUseTrick(board, trickName);
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

    @JavascriptInterface
    public int getAnkiCoins() {
        return getCoins();
    }

    @JavascriptInterface
    public void hasLost() {
        getMvpView().postRunnable(new Runnable() {
            @Override
            public void run() {
                getMvpView().showHasLostDialog();
            }
        });
    }

    @JavascriptInterface
    public void hasWon(String jsonString) {
        getMvpView().postRunnable(new Runnable() {
            @Override
            public void run() {
                getMvpView().showHasWonDialog();
                Board board =  Board.parseJSON(jsonString);
                log(board, GameLog.TYPE_WON_GAME);
            }
        });
    }

    private GameLog logWonGame(Board board) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        gameLog.setLogType(GameLog.TYPE_WON_GAME);
        gameLog.setBestScore(board.getBestScore());
        gameLog.setCurrentScore(board.getScore());
        gameLog.setUsedTricks(board.getUsedTricksAsString());
        gameLog.setBoardValues(board.getBoardValuesAsString());
        gameLog.setTotalCoins(getCoins());
        gameLog.setTotalPoints(getPoints());
        // TODO: AnkiGame Add leaderboard position

        return gameLog;
    }

    private GameLog logGoToAnki(Board board) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        gameLog.setLogType(GameLog.TYPE_GO_TO_ANKI);
        gameLog.setBestScore(board.getBestScore());
        gameLog.setCurrentScore(board.getScore());
        gameLog.setUsedTricks(board.getUsedTricksAsString());
        gameLog.setBoardValues(board.getBoardValuesAsString());
        gameLog.setTotalCoins(getCoins());
        gameLog.setTotalPoints(getPoints());
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
        gameLog.setTotalCoins(getCoins());
        gameLog.setTotalPoints(getPoints());
        // TODO: AnkiGame Add leaderboard position

        return gameLog;
    }

    private GameLog logLostGame(Board board) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        gameLog.setLogType(GameLog.TYPE_LOST_GAME);
        gameLog.setBestScore(board.getBestScore());
        gameLog.setCurrentScore(board.getScore());
        gameLog.setUsedTricks(board.getUsedTricksAsString());
        gameLog.setBoardValues(board.getBoardValuesAsString());
        gameLog.setTotalCoins(getCoins());
        gameLog.setTotalPoints(getPoints());
        // TODO: AnkiGame Add leaderboard position

        return gameLog;
    }

    private void logUseTrick(Board board, String trickName) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        gameLog.setLogType(GameLog.TYPE_USE_TRICK);
        gameLog.setBestScore(board.getBestScore());
        gameLog.setCurrentScore(board.getScore());
        gameLog.setUsedTricks(board.getUsedTricksAsString());
        gameLog.setBoardValues(board.getBoardValuesAsString());
        gameLog.setTotalCoins(getCoins());
        gameLog.setTotalPoints(getPoints());
        gameLog.setTrickType(trickName);

        mDataManager.logBehaviour(gameLog);
    }

    private void logFailTrick(Board board, String trickName, String failTrickType) {
        GameLog gameLog = GameLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        gameLog.setLogType(GameLog.TYPE_FAIL_TRICK);
        gameLog.setBestScore(board.getBestScore());
        gameLog.setCurrentScore(board.getScore());
        gameLog.setUsedTricks(board.getUsedTricksAsString());
        gameLog.setBoardValues(board.getBoardValuesAsString());
        gameLog.setTotalCoins(getCoins());
        gameLog.setTotalPoints(getPoints());
        gameLog.setTrickType(trickName);
        gameLog.setFailTrickType(failTrickType);

        mDataManager.logBehaviour(gameLog);
    }

}
