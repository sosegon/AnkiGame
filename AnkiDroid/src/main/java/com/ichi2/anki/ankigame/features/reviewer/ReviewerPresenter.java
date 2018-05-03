package com.ichi2.anki.ankigame.features.reviewer;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;

import javax.inject.Inject;

public class ReviewerPresenter extends BasePresenter<ReviewerMvpView> {
    private final DataManager mDataManager;

    @Inject
    public ReviewerPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }
}