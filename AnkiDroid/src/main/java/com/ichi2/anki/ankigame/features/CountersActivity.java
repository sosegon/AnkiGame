package com.ichi2.anki.ankigame.features;

import android.view.View;
import android.widget.TextView;

import com.ichi2.anki.NavigationDrawerActivity;
import com.ichi2.anki.R;

public class CountersActivity extends NavigationDrawerActivity {
    TextView mLblCoins;
    TextView mLblPoints;

    protected void initCounters(View maiView) {
        mLblCoins = maiView.findViewById(R.id.lbl_coins_game);
        mLblPoints = maiView.findViewById(R.id.lbl_points);
    }

    public void updateLblGameCoins(int coins) {
        if(mLblCoins != null) {
            mLblCoins.setText(getString(R.string.coins, coins));
        }
    }

    public void updateLblPoints(int points) {
        if(mLblPoints != null) {
            mLblPoints.setText(getString(R.string.points, points));
        }
    }
}
