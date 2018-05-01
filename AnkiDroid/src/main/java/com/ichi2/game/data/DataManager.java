package com.ichi2.game.data;

import com.ichi2.game.data.local.PreferencesHelper;
import com.ichi2.game.util.RxEventBus;

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
