package com.ichi2.anki.ankigame.features.deckpicker;

import android.content.Context;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.injection.ApplicationContext;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class DeckPickerPresenter extends BasePresenter<DeckPickerMvpView> {
    private final static int DAYS_UNTIL_PROMPT = 15;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 20;//Min number of launches

    private final DataManager mDataManager;
    private Context mContext;

    @Inject
    public DeckPickerPresenter(@ApplicationContext Context context, DataManager dataManager) {
        this.mContext = context;
        mDataManager = dataManager;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
    }

    public String getShareUrl() {
        return mDataManager.getShareUrl();
    }

    public void logGoToGame() {
        AnkiLog ankiLog = AnkiLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setLogType(AnkiLog.TYPE_GO_TO_GAME);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setEarnedPoints(getEarnedPoints());
        ankiLog.setEarnedCoins(getEarnedCoins());
        mDataManager.logBehaviour(ankiLog);
    }

    public String initUser() {
        return mDataManager.initUser();
    }

    public void addNewLaunch() {
        boolean showRater = mDataManager.getPreferencesHelper().retrieveShowRater();
        if(showRater == false) {
            return;
        }

        long launches = mDataManager.getPreferencesHelper().retrieveLaunches();
        mDataManager.getPreferencesHelper().storeLaunches(launches + 1);

        Long firstLaunchDate = mDataManager.getPreferencesHelper().retrieveFirstLaunchDate();
        if(firstLaunchDate == 0) {
            firstLaunchDate = System.currentTimeMillis();
            mDataManager.getPreferencesHelper().storeFirstLaunchDate(firstLaunchDate);
        }

        if(launches >= LAUNCHES_UNTIL_PROMPT) {
            if(System.currentTimeMillis() >= firstLaunchDate + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                getMvpView().showRateDialog();
            }
        }
    }

    public void avoidRater(){
        mDataManager.getPreferencesHelper().storeShowRater(false);
    }

    public String getAppPackageName(){
        return mContext.getPackageName();
    }

    private int getEarnedCoins() {
        return mDataManager.getPreferencesHelper().retrieveEarnedCoins();
    }

    private int getEarnedPoints() {
        return mDataManager.getPreferencesHelper().retrieveEarnedPoints();
    }

}
