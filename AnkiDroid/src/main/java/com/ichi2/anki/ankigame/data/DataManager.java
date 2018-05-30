package com.ichi2.anki.ankigame.data;

import com.google.firebase.database.DatabaseReference;
import com.ichi2.anki.ankigame.data.local.PreferencesHelper;
import com.ichi2.anki.ankigame.data.model.AppLog;
import com.ichi2.anki.ankigame.data.model.User;
import com.ichi2.anki.ankigame.data.remote.Analytics;
import com.ichi2.anki.ankigame.data.remote.FirebaseHelper;
import com.ichi2.anki.ankigame.util.RxEventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.ichi2.anki.ankigame.data.remote.FirebaseHelper.USERLOGS_KEY;

@Singleton
public class DataManager {

    private static final String LOG_TAG = DataManager.class.getSimpleName();
    private final PreferencesHelper mPreferencesHelper;
    private final FirebaseHelper mFirebaseHelper;
    private final Analytics mAnalytics;
    private final RxEventBus mEventBus;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, Analytics analytics, RxEventBus eventBus) {
        mPreferencesHelper = preferencesHelper;
        mFirebaseHelper = new FirebaseHelper(); // Does not depend on anything
        mAnalytics = analytics;
        mEventBus = eventBus;
        initBus();
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public FirebaseHelper getFirebaseHelper() {
        return mFirebaseHelper;
    }

    public Analytics getAnalytics() {
        return mAnalytics;
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

    public void logBehaviour(AppLog log) {
        if(log != null) {
            DatabaseReference logRef = mFirebaseHelper.storeLog(log);

            // Store the reference of the log under user record
            DatabaseReference userRef = mFirebaseHelper.retrieveUser(log.getUserId());
            userRef.child(USERLOGS_KEY).child(logRef.getKey()).setValue(true);
        }
    }

    public void updateBestScore(int bestScore) {
        mPreferencesHelper.storeBestScore(bestScore);
        String userId = mPreferencesHelper.retrieveUserId();
        mFirebaseHelper.storeBestScore(userId, bestScore);
    }

    private void initBus() {
        // Subscribe consumers
    }

}
