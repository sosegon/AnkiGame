package com.ichi2.anki.ankigame.features.customankimal;

import com.ichi2.anki.ankigame.base.MvpView;

public interface CustomAnkimalMvpView extends MvpView {
    void showInsufficientCoinsMessage(int requiredCoins);
    void updatePlayerIcon();
    void updateSelectViews();
    void updateCoins();
}
