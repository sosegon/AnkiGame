package com.ichi2.anki.ankigame.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ichi2.anki.ankigame.data.local.PreferencesHelper;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.data.model.AnkiString;
import com.ichi2.anki.ankigame.data.model.AppLog;
import com.ichi2.anki.ankigame.data.model.User;
import com.ichi2.anki.ankigame.data.remote.Analytics;
import com.ichi2.anki.ankigame.data.remote.FirebaseHelper;
import com.ichi2.anki.ankigame.util.AnkimalsUtils;
import com.ichi2.anki.ankigame.util.InfoHandler;
import com.ichi2.anki.ankigame.util.RxEventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private AnkiString mSurveyUrl;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, FirebaseHelper firebaseHelper, Analytics analytics, RxEventBus eventBus) {
        mPreferencesHelper = preferencesHelper;
        mFirebaseHelper = firebaseHelper;
        mAnalytics = analytics;
        mEventBus = eventBus;
        initBus();
        initSurveyUrl();
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

//        if(BuildConfig.DEBUG) {
//            mPreferencesHelper.clear();
//            userId = mPreferencesHelper.retrieveUserId();
//        }

        // Create new user if necessary
        if(userId.equals("") || userId.equals("anon")) {

            // Set a nickname here
            String randomSuffix = String.valueOf((int)(Math.random() * 1000));
            mPreferencesHelper.storeNickName("player" + randomSuffix);

            User newUser = new User();
            newUser.setBestScore(mPreferencesHelper.retrieveBestScore());
            newUser.setNickName(mPreferencesHelper.retrieveNickName());
            newUser.setPoints(mPreferencesHelper.retrievePoints());

            int ankimalIndex = mPreferencesHelper.retrieveLastSelectedAnkimal();
            newUser.setColoredAnkimal(AnkimalsUtils.isColoredAnkimal(this, ankimalIndex));
            newUser.setAnkimalIndex(ankimalIndex + 1); // workaround for users with no avatar set

            Date date = new Date();
            Date newDate = new Date(date.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String sDate = dateFormat.format(newDate);
            String sTime = timeFormat.format(newDate);

            newUser.setDate(sDate);
            newUser.setTime(sTime);
            mPreferencesHelper.storeUserDate(sDate);
            mPreferencesHelper.storeUserTime(sTime);

            DatabaseReference userRef = mFirebaseHelper.storeUser(newUser);

            // Store the key locally
            userId = userRef.getKey();
            mPreferencesHelper.storeUserId(userId);
        }

        return userId;
    }

    public void logBehaviour(AppLog log) {
//        if(true) return;
        if(log != null) {

            if(log instanceof AnkiLog) {
                String deckInfo = ((AnkiLog)(log)).getDeckInfo();
                if(deckInfo != null) {
                    String filterDeckInfo = InfoHandler.filterDeckInfo(deckInfo);
                    ((AnkiLog)(log)).setDeckInfo(filterDeckInfo);
                }

                String cardInfo = ((AnkiLog)(log)).getCardInfo();
                if(cardInfo != null) {
                    String filterCardInfo = InfoHandler.filterCardInfo(cardInfo);
                    ((AnkiLog)(log)).setCardInfo(filterCardInfo);
                }
            }

            DatabaseReference logRef = mFirebaseHelper.storeLog(log);

            // Store the reference of the log under user record
            DatabaseReference userRef = mFirebaseHelper.retrieveUser(log.getUserId());
            userRef.child(USERLOGS_KEY).child(logRef.getKey()).setValue(log.getLogType());

            mAnalytics.logEvent(log);
        }
    }

    public void updateBestScore(int bestScore) {
        mPreferencesHelper.storeBestScore(bestScore);
        String userId = mPreferencesHelper.retrieveUserId();
        mFirebaseHelper.storeBestScore(userId, bestScore);
    }

    public String getSurveyUrl(){
        return mSurveyUrl.getString();
    }

    private void initSurveyUrl() {
        mSurveyUrl = new AnkiString();
        mFirebaseHelper.retrieveSurveyUrl().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSurveyUrl.setString(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initBus() {
        // Subscribe consumers
    }

}
