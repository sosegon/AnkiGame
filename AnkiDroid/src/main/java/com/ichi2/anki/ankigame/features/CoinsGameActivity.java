package com.ichi2.anki.ankigame.features;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ichi2.anki.NavigationDrawerActivity;
import com.ichi2.anki.R;

public class CoinsGameActivity extends NavigationDrawerActivity {
    TextView mLblCoins;

    protected void initCoinsBar(View maiView) {
        mLblCoins = maiView.findViewById(R.id.lbl_coins_game);
    }

    public void updateLblGameCoins(int coins) {
        if(mLblCoins != null) {
            mLblCoins.setText(getString(R.string.coins, coins));
        }
    }
}
