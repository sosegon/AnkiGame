package com.ichi2.game.data;

import com.ichi2.game.data.local.PreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }
}
