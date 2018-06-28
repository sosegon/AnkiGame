package com.ichi2.anki.ankigame.features.game;

import com.ichi2.anki.ankigame.base.MvpView;

public interface GameMvpView extends MvpView {
    void updateLblGameCoins(int coins, boolean increase); // Implemented in CountersActivity, but still needed so
                                        // the presenter can invoke it from the interface
    void showInsufficientCoinsMessage(int requiredCoins);
    void showInsufficientPointsMessage(int requiredPoints);
    void showUnableToDoTrickMessage(String trickName);
    void postRunnable(Runnable runnable);
    void showHasLostDialog();
    void showHasWonDialog();
}
