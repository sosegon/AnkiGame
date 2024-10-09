package com.ichi2.anki.ankigame.features.customankimal;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.injection.ApplicationContext;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;
import com.ichi2.anki.ankigame.util.AnkimalsUtils;

import javax.inject.Inject;

@ConfigPersistent
public class CustomAnkimalPresenter extends BasePresenter<CustomAnkimalMvpView> {

    private DataManager mDataManager;
    private Context mContext;
    private int mAnkimalIndex = -1;
    public static final int REQUIRED_COINS_TO_SELECT = 50;
    public static final int REQUIRED_COINS_TO_COLOR = 25;

    @Inject
    public CustomAnkimalPresenter(@ApplicationContext Context context, DataManager dataManager) {
        mDataManager = dataManager;
        mContext = context;
        mAnkimalIndex = mDataManager.getPreferencesHelper().retrieveLastSelectedAnkimal();
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
    }

    public void colorAnkimal() {
        if(mAnkimalIndex != -1) {
            if(!hasRequiredCoinsToColor()) {
                getMvpView().showInsufficientCoinsMessage(REQUIRED_COINS_TO_COLOR - getCoins());
            } else {
                String sColoredAnkimals = getColoredAnkimals();
                int[] iColoredAnkimals = AnkimalsUtils.convertAnkimals(sColoredAnkimals);
                sColoredAnkimals = updateAnkimals(iColoredAnkimals, mAnkimalIndex);
                mDataManager.getPreferencesHelper().storeColoredAnkimals(sColoredAnkimals);

                int updatedCoins = getCoins() - REQUIRED_COINS_TO_COLOR;
                mDataManager.getPreferencesHelper().storeCoins(updatedCoins);
                getMvpView().updateCoins();

                getMvpView().updateColorViews();
                getMvpView().doColorEffect();
                getMvpView().updateAnkimalList();
                if(isCurrentlySelected()) {
                    getMvpView().updatePlayerIcon();
                    mDataManager.getFirebaseHelper().storeUserColoredAnkimal(getUserId(), true);
                }

                logColorAnkimal();
            }
        }
    }

    public void selectAnkimal() {
        if(mAnkimalIndex != -1) {

            boolean selected = false;
            if(wasPreviouslySelected()) {
                selected = true;
            } else {
                if(!hasRequiredCoinsToSelect()) {
                    getMvpView().showInsufficientCoinsMessage(REQUIRED_COINS_TO_SELECT - getCoins());
                } else {
                    String sSelectedAnkimals = getSelectedAnkimals();
                    int[] iSelectedAnkimals = AnkimalsUtils.convertAnkimals(sSelectedAnkimals);
                    sSelectedAnkimals = updateAnkimals(iSelectedAnkimals, mAnkimalIndex);
                    mDataManager.getPreferencesHelper().storeSelectedAnkimals(sSelectedAnkimals);

                    int updatedCoins = getCoins() - REQUIRED_COINS_TO_SELECT;
                    mDataManager.getPreferencesHelper().storeCoins(updatedCoins);
                    getMvpView().updateCoins();
                    selected = true;
                }
            }

            if(selected){
                mDataManager.getPreferencesHelper().storeLastSelectedAnkimal(mAnkimalIndex);
                getMvpView().updatePlayerIcon();
                getMvpView().updateSelectViews();
                getMvpView().doSelectEffect();

                String userId = getUserId();
                int ankimalIndex = mDataManager.getPreferencesHelper().retrieveLastSelectedAnkimal();
                boolean coloredAnkimal = AnkimalsUtils.isColoredAnkimal(mDataManager, ankimalIndex);
                ankimalIndex += 1; // workaround for users with no avatar set

                mDataManager.getFirebaseHelper().storeUserAnkimalIndex(userId, ankimalIndex);
                mDataManager.getFirebaseHelper().storeUserColoredAnkimal(userId, coloredAnkimal);

                logSelectAnkimal();
            }
        }
    }

    public void setAnkimalIndex(int index) {
        mAnkimalIndex = index;
    }

    public boolean hasRequiredCoinsToSelect() {
        return getCoins() >= REQUIRED_COINS_TO_SELECT;
    }

    public boolean wasPreviouslySelected() {
        int[] iSelectedAnkimals = AnkimalsUtils.convertAnkimals(getSelectedAnkimals());
        for(int prevAnkimal : iSelectedAnkimals) {
            if(prevAnkimal == mAnkimalIndex){
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentlySelected() {
        return mAnkimalIndex == getLastSelectedAnkimal();
    }

    public boolean hasRequiredCoinsToColor() {
        return getCoins() >= REQUIRED_COINS_TO_COLOR;
    }

    public boolean isColored() {
        return AnkimalsUtils.isColoredAnkimal(mDataManager, mAnkimalIndex);
    }

    public Drawable getDrawableAnkimal() {
        boolean colored = AnkimalsUtils.isColoredAnkimal(mDataManager, mAnkimalIndex);
        return AnkimalsUtils.getDrawableAnkimal(mContext, mAnkimalIndex, colored);
    }

    private int getLastSelectedAnkimal() {
        return mDataManager.getPreferencesHelper().retrieveLastSelectedAnkimal();
    }

    private String getSelectedAnkimals() {
        return mDataManager.getPreferencesHelper().retrieveSelectedAnkimals();
    }

    private String getColoredAnkimals() {
        return mDataManager.getPreferencesHelper().retrieveColoredAnkimals();
    }

    private String updateAnkimals(int[] existingAnkimals, int newAnkimal) {
        String ankimals = "";
        for(int i = 0; i < existingAnkimals.length; i++) {
            ankimals += String.valueOf(existingAnkimals[i]) + ",";
        }

        ankimals += String.valueOf(newAnkimal);

        return ankimals;
    }

    private void logSelectAnkimal() {
        String ankimalName = AnkimalsUtils.getAnkimalName(mContext, mAnkimalIndex);
        AnkiLog ankiLog = AnkiLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setLogType(AnkiLog.TYPE_SELECT_ANKIMAL);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setAnkimalName(ankimalName);

        mDataManager.logBehaviour(ankiLog);
    }

    private void logColorAnkimal() {
        String ankimalName = AnkimalsUtils.getAnkimalName(mContext, mAnkimalIndex);
        AnkiLog ankiLog = AnkiLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setLogType(AnkiLog.TYPE_COLOR_ANKIMAL);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setAnkimalName(ankimalName);

        mDataManager.logBehaviour(ankiLog);
    }

    private String getUserId() {
        return mDataManager.getPreferencesHelper().retrieveUserId();
    }
}
