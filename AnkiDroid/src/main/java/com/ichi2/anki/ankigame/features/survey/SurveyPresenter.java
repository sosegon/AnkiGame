package com.ichi2.anki.ankigame.features.survey;

import android.net.Uri;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class SurveyPresenter extends BasePresenter<SurveyMvpView> {

    private final DataManager mDataManager;

    @Inject
    public SurveyPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public Uri getSurveyUrl() {
        String url = mDataManager.getSurveyUrl();
        return Uri.parse(url);
    }

    public boolean tookSurvey() {
        return mDataManager.getPreferencesHelper().retrieveSurveyTaken();
    }

    public void surveyTaken() {
        mDataManager.getPreferencesHelper().storeSurveyTaken(true);
    }
}
