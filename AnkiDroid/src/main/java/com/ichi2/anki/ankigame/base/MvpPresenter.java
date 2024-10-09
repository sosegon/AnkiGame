package com.ichi2.anki.ankigame.base;

/**
 * Based on code from https://github.com/ribot/android-boilerplate
 */

public interface MvpPresenter<V extends MvpView> {
    void attachView(V mpvView);
    void detachView();
}
