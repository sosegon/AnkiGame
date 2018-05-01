package com.ichi2.game;

import com.ichi2.game.base.MvpView;

public interface GameMvpView extends MvpView {
    void updateLblGameCoins(int coins);
    void showNoCoinsToast();
}
