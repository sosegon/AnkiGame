package com.ichi2.anki.ankigame.features.game;

import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.ichi2.anki.BuildConfig;
import com.ichi2.anki.ankigame.data.model.Board;
import com.ichi2.anki.ankigame.util.RxEventBus;

public class GameJsInterface {
    GameMvpView mGameView;
    GamePresenter mGamePresenter;
    RxEventBus mEventBus;
    Handler mHandler;

    public static final String TRICK_BOMB = "bomb";
    public static final String TRICK_GIFT = "gift";
    public static final String TRICK_UNDO = "undo";
    public static final String TRICK_DOUBLE = "double";

    public GameJsInterface(Handler handler, GameMvpView gameView, GamePresenter gamePresenter) {
        this.mGameView = gameView;
        this.mGamePresenter = gamePresenter;
        this.mHandler = handler;
    }

    @JavascriptInterface
    public boolean hasMoneyForTrick(String trickName, String jsonString) {
        boolean r = false;
        Board board = Board.parseJSON(jsonString);
        int coins =  mGamePresenter.getCoins();
        int requiredCoins = -1;

        if(BuildConfig.FLAVOR == "independent") {
            mGamePresenter.logUseTrick(board, trickName, true);
            return true;
        }

        // TODO: AnkiGame, Review the required coins for each trick
        if(trickName.contentEquals(TRICK_BOMB)) {
            requiredCoins = 10;
        } else if(trickName.contentEquals(TRICK_GIFT)) {
            requiredCoins = 10;
        } else if(trickName.contentEquals(TRICK_UNDO)) {
            requiredCoins = 10;
        } else if(trickName.contentEquals(TRICK_DOUBLE)) {
            requiredCoins = 10;
        }

        if(requiredCoins > 0 && coins >= requiredCoins) {
            r = true;
            mGamePresenter.reduceCoins(requiredCoins);
            // WebView has its own threads, therefore, updates in the UI has to be done accordingly
            // The following line has to be executed in a handler created in the activity
            // mGameView.updateLblGameCoins(mGamePresenter.getCoins());
            mHandler.post(new Runnable(){
                @Override
                public void run () {
                    mGameView.updateLblGameCoins(mGamePresenter.getCoins());
                    mGamePresenter.logUseTrick(board, trickName, true);
                }
            });
        } else {
            mHandler.post(new Runnable(){
                @Override
                public void run () {
                    // TODO: AnkiGame, Fix this not working toast
                    mGameView.showNoCoinsToast();
                    mGamePresenter.logUseTrick(board, trickName, false);
                }
            });
        }

        return r;
    }

    @JavascriptInterface
    public void getBoardState(String jsonString, int logType) {
        mHandler.post(new Runnable(){
            @Override
            public void run () {
                Board board =  Board.parseJSON(jsonString);
                mGamePresenter.log(board, logType);
            }
        });
    }

    @JavascriptInterface
    public void updateBestScore(int bestScore) {
        mHandler.post(new Runnable(){
            @Override
            public void run () {
                mGamePresenter.updateBestScore(bestScore);
            }
        });
    }

    @JavascriptInterface
    public void unableToDoTrick(String trickName, String jsonString) {
        Board board = Board.parseJSON(jsonString);
        mHandler.post(new Runnable(){
            @Override
            public void run () {
                mGamePresenter.logUseTrick(board, trickName, false);
            }
        });
    }
}
