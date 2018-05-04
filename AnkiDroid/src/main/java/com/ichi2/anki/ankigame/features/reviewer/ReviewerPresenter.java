package com.ichi2.anki.ankigame.features.reviewer;

import android.support.v4.content.ContextCompat;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;

import javax.inject.Inject;

import static com.ichi2.anki.AbstractFlashcardViewer.EASE_1;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_2;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_3;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_4;

public class ReviewerPresenter extends BasePresenter<ReviewerMvpView> {
    private final DataManager mDataManager;

    @Inject
    public ReviewerPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int increaseCoins(int ease) {
        int currentCoins = 0;

        // TODO: ANKIGAME, Double check the values
        switch (ease) {
            case EASE_1:
                currentCoins += 1;
                break;
            case EASE_2:
                currentCoins += 2;
                break;
            case EASE_3:
                currentCoins += 3;
                break;
            case EASE_4:
                currentCoins += 4;
                break;
        }

        mDataManager.getPreferencesHelper().updateCoins(currentCoins);

        return mDataManager.getPreferencesHelper().retrieveCoins();
    }
}