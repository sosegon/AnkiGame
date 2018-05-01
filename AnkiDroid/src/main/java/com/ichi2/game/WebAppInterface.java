package com.ichi2.game;

import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.ichi2.game.util.RxEvent;
import com.ichi2.game.util.RxEventBus;

import static com.ichi2.game.util.RxEvent.RX_EVENT_TYPE.COINS_UPDATED;

public class WebAppInterface {
    GameMvpView mGameView;
    GamePresenter mGamePresenter;
    RxEventBus mEventBus;
    Handler mHandler;

    public WebAppInterface(Handler handler, GameMvpView gameView, GamePresenter gamePresenter) {
        this.mGameView = gameView;
        this.mGamePresenter = gamePresenter;
        this.mHandler = handler;
    }

    @JavascriptInterface
    public boolean hasMoneyForTrick(String trickName) {
        boolean r = false;
        int coins =  mGamePresenter.getCoins();
        int requiredCoins = -1;
        if(trickName.contentEquals("bomb")) {
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
                }
            });
        } else {
            mHandler.post(new Runnable(){
                @Override
                public void run () {
                    mGameView.showNoCoinsToast();
                }
            });
        }

        return r;
    }
}
