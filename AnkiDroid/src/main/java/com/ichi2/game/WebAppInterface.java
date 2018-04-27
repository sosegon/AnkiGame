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
        // TODO: AnkiGame, use this value to return true or false
        int coins =  mGamePresenter.getCoins();
        return true;
    }
}
