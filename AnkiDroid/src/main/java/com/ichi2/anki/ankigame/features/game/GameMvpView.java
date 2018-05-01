package com.ichi2.anki.ankigame.features.game;

import com.ichi2.anki.ankigame.base.MvpView;

public interface GameMvpView extends MvpView {
    void updateLblGameCoins(int coins);
    void showNoCoinsToast();
}
