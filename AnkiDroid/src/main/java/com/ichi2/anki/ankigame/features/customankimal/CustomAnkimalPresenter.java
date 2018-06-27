package com.ichi2.anki.ankigame.features.customankimal;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;

import java.util.Arrays;

import javax.inject.Inject;

public class CustomAnkimalPresenter extends BasePresenter<CustomAnkimalMvpView> {

    private DataManager mDataManager;
    private int mAnkimalIndex = -1;
    public static final int REQUIRED_COINS_TO_SELECT = 50;

    @Inject
    public CustomAnkimalPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mAnkimalIndex = mDataManager.getPreferencesHelper().retrieveLastSelectedAnkimal();
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
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
                    int[] iSelectedAnkimals = convertSelectedAnimals(sSelectedAnkimals);
                    sSelectedAnkimals = updateSelectedAnkimals(iSelectedAnkimals, mAnkimalIndex);
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
                // TODO: Ankigame logging
            }
        }
    }

    public int getAnkimalIndex() {
        return mAnkimalIndex;
    }

    public void setAnkimalIndex(int index) {
        mAnkimalIndex = index;
    }

    public boolean hasRequiredCoinsToSelect() {
        return getCoins() >= REQUIRED_COINS_TO_SELECT;
    }

    public boolean wasPreviouslySelected() {
        int[] iSelectedAnkimals = convertSelectedAnimals(getSelectedAnkimals());
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

    private int getLastSelectedAnkimal() {
        return mDataManager.getPreferencesHelper().retrieveLastSelectedAnkimal();
    }

    private int[] convertSelectedAnimals(String selectedAnkimals) {
        if(selectedAnkimals.contentEquals("")){
            return new int[0];
        }

        String[] sAnkimals = selectedAnkimals.split(",");
        int[] iAnkimals = new int[sAnkimals.length];
        for(int i = 0; i < sAnkimals.length; i++) {
            if(sAnkimals[i].contentEquals("")) {
                iAnkimals[i] = -1;
                continue;
            }
            iAnkimals[i] = Integer.valueOf(sAnkimals[i]);
        }

        return iAnkimals;
    }

    private String getSelectedAnkimals() {
        return mDataManager.getPreferencesHelper().retrieveSelectedAnkimals();
    }

    private String updateSelectedAnkimals(int[] existingAnkimals, int newAnkimal) {
        String ankimals = "";
        for(int i = 0; i < existingAnkimals.length; i++) {
            ankimals += String.valueOf(existingAnkimals[i]) + ",";
        }

        ankimals += String.valueOf(newAnkimal);

        return ankimals;
    }
}
