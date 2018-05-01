package com.ichi2.anki.ankigame.data;

import com.ichi2.anki.ankigame.data.local.PreferencesHelper;
import com.ichi2.anki.ankigame.util.RxEventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {
    private final PreferencesHelper mPreferencesHelper;
    private final RxEventBus mEventBus;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, RxEventBus eventBus) {
        mPreferencesHelper = preferencesHelper;
        mEventBus = eventBus;
        initBus();
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    private void initBus() {
        // Subscribe consumers
    }
}
