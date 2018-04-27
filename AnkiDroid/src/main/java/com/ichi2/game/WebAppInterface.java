package com.ichi2.game;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    public WebAppInterface(Context mContext) {
        this.mContext = mContext;
    }

    @JavascriptInterface
    public boolean hasMoneyForTrick(String trickName) {
        // TODO: AnkiGame, get this value from the preferences.
        return true;
    }
}
