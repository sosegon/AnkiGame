package com.ichi2.anki.ankigame.features.customstudy;

import com.ichi2.anki.AnkiDroidApp;
import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;
import com.ichi2.libanki.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import javax.inject.Inject;

import static com.ichi2.anki.ankigame.features.customstudy.CustomStudyDialog.CUSTOM_STUDY_AHEAD;
import static com.ichi2.anki.ankigame.features.customstudy.CustomStudyDialog.CUSTOM_STUDY_FORGOT;
import static com.ichi2.anki.ankigame.features.customstudy.CustomStudyDialog.CUSTOM_STUDY_NEW;
import static com.ichi2.anki.ankigame.features.customstudy.CustomStudyDialog.CUSTOM_STUDY_PREVIEW;
import static com.ichi2.anki.ankigame.features.customstudy.CustomStudyDialog.CUSTOM_STUDY_RANDOM;
import static com.ichi2.anki.ankigame.features.customstudy.CustomStudyDialog.CUSTOM_STUDY_REV;
import static com.ichi2.anki.ankigame.features.customstudy.CustomStudyDialog.CUSTOM_STUDY_TAGS;

@ConfigPersistent
public class CustomStudyDialogPresenter extends BasePresenter<CustomStudyDialogMvpView> {
    private final DataManager mDataManager;
    private String mDeckInfo;

    @Inject
    public CustomStudyDialogPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void logCustomStudy(int option) {
        AnkiLog ankiLog = AnkiLog.logBase(getUserId());
        ankiLog.setLogType(AnkiLog.TYPE_CUSTOM_STUDY);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setDeckInfo(mDeckInfo);
        // TODO: AnkiGame, add logic to logEvent the earned coins as well.

        String sOption = "";
        switch (option) {
            case CUSTOM_STUDY_NEW:
                sOption = "increaseTodayNewCardLimit";
                break;
            case CUSTOM_STUDY_REV:
                sOption = "increaseTodayReviewCardLimit";
                break;
            case CUSTOM_STUDY_FORGOT:
                sOption = "reviewForgottenCards";
                break;
            case CUSTOM_STUDY_AHEAD:
                sOption = "reviewAhead";
                break;
            case CUSTOM_STUDY_RANDOM:
                sOption = "studyRandomSelectionCards";break;
            case CUSTOM_STUDY_PREVIEW:
                sOption = "previewNewCard";
                break;
            case CUSTOM_STUDY_TAGS:
                sOption = "limitParticularTags";
                break;
            default:
                break;
        }
        ankiLog.setCustomStudyOption(sOption);

        mDataManager.logBehaviour(ankiLog);
    }

    public void setDeckInfo(String mDeckInfo) {
        this.mDeckInfo = mDeckInfo;
    }

    private int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    private int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
    }

    private String getUserId() {
        return mDataManager.getPreferencesHelper().retrieveUserId();
    }

}
