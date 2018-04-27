package com.ichi2.game;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;
    GamePresenter mGamePresenter;

    public WebAppInterface(Context mContext, GamePresenter gamePresenter) {
        this.mContext = mContext;
        this.mGamePresenter = gamePresenter;
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
        } else {
            // TODO: AnkiGame, toast not working
            Toast.makeText(mContext, "Not enough coins", Toast.LENGTH_SHORT);
        }

        return r;
    }
}
