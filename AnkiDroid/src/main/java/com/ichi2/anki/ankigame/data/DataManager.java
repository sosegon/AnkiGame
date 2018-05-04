package com.ichi2.anki.ankigame.data;

import com.ichi2.anki.ankigame.data.local.PreferencesHelper;
import com.ichi2.anki.ankigame.data.remote.FirebaseHelper;
import com.ichi2.anki.ankigame.util.RxEventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {
    private final PreferencesHelper mPreferencesHelper;
    private final FirebaseHelper mFirebaseHelper;
    private final RxEventBus mEventBus;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, FirebaseHelper firebaseHelper, RxEventBus eventBus) {
        mPreferencesHelper = preferencesHelper;
        mFirebaseHelper = firebaseHelper;
        mEventBus = eventBus;
        initBus();
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public FirebaseHelper getFirebaseHelper() {
        return mFirebaseHelper;
    }

    private void initBus() {
        // Subscribe consumers
    }
}
