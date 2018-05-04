package com.ichi2.anki.ankigame.data;

import com.google.firebase.database.DatabaseReference;
import com.ichi2.anki.ankigame.data.local.PreferencesHelper;
import com.ichi2.anki.ankigame.data.model.User;
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
    public DataManager(PreferencesHelper preferencesHelper, RxEventBus eventBus) {
        mPreferencesHelper = preferencesHelper;
        mFirebaseHelper = new FirebaseHelper(); // Does not depend on anything
        mEventBus = eventBus;
        initBus();
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public FirebaseHelper getFirebaseHelper() {
        return mFirebaseHelper;
    }

    public String initUser() {
        String userId = mPreferencesHelper.retrieveUserId();

        // Create new user if necessary
        if(userId.equals("")) {
            DatabaseReference userRef = mFirebaseHelper.storeUser(new User(""));

            // Store the key locally
            userId = userRef.getKey();
            mPreferencesHelper.storeUserId(userId);
        }

        return userId;
    }

    private void initBus() {
        // Subscribe consumers
    }
}
