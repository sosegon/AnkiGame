package com.ichi2.anki.ankigame.features.reviewer;

import android.graphics.drawable.Drawable;

import com.ichi2.anki.ankigame.base.MvpView;

public interface ReviewerMvpView extends MvpView {
    void showLiberatedAnimalMessage(Drawable icon);
}
