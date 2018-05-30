package com.ichi2.anki.ankigame.features.game;

import com.ichi2.anki.ankigame.base.MvpView;

public interface GameMvpView extends MvpView {
    void updateLblGameCoins(int coins); // Implemented in CoinsGameActivity, but still needed so
                                        // the presenter can invoke it from the interface
    void showNoCoinsToast(int requiredCoins);
    void postRunnable(Runnable runnable);
    void showUnableToDoTrickToast(String trickName);
}
