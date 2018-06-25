package com.ichi2.anki.ankigame.data.remote;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ichi2.anki.BuildConfig;
import com.ichi2.anki.ankigame.data.model.AppLog;
import com.ichi2.anki.ankigame.data.model.User;

import javax.inject.Singleton;

@Singleton
public class FirebaseHelper {
    // Database
    public static final String CONNECTION_KEY = "connection";
    public static final String INDEPENDENT_KEY = "independent";
    public static final String USERS_KEY = "users";
    public static final String LOGS_KEY = "logs";
    public static final String USERLOGS_KEY = "logs";
    public static final String BEST_SCORE_KEY = User.PARAM_BEST_SCORE;
    public static final String POINTS_KEY = User.PARAM_POINTS;
    public static final String NICK_NAME_KEY = User.PARAM_NICK_NAME;
    public static final String USER_DATE_KEY = User.PARAM_DATE;
    public static final String USER_TIME_KEY = User.PARAM_TIME;
    public static final String SHARE_URL_KEY = "shareUrl";
    public static final String SURVEY_URL_KEY = "surveyUrl";

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mLogsDatabaseReference;
    private DatabaseReference mRoot;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseMessaging mFirebaseMessaging;

    public FirebaseHelper() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        if(BuildConfig.FLAVOR == "connection") {
            mRoot = mFirebaseDatabase.getReference().child(CONNECTION_KEY);
        } else {
            mRoot = mFirebaseDatabase.getReference().child(INDEPENDENT_KEY);

        }
        mUsersDatabaseReference = mRoot.child(USERS_KEY);
        mLogsDatabaseReference = mRoot.child(LOGS_KEY);

        mFirebaseMessaging = FirebaseMessaging.getInstance();
        subscribeToAnkimals();
    }

    public DatabaseReference storeUser(User user) {
        DatabaseReference userRef = mUsersDatabaseReference.push();
        userRef.setValue(user);
        return userRef;
    }

    public DatabaseReference storeLog(AppLog appLog) {
        //DatabaseReference logRef = mLogsDatabaseReference.push();
        DatabaseReference logTypeRef = mLogsDatabaseReference.child(String.valueOf(appLog.getLogType())).push();
        logTypeRef.setValue(appLog);
        return logTypeRef;
    }

    public DatabaseReference retrieveUser(String userId) {
        return mUsersDatabaseReference.child(userId);
    }

    public DatabaseReference storeBestScore(String userId, int bestScore) {
        DatabaseReference userRef = retrieveUser(userId);
        userRef.child(BEST_SCORE_KEY).setValue(bestScore);

        return userRef.child(BEST_SCORE_KEY);
    }

    public DatabaseReference storePoints(String userId, int points) {
        DatabaseReference userRef = retrieveUser(userId);
        userRef.child(POINTS_KEY).setValue(points);

        return userRef.child(POINTS_KEY);
    }

    public DatabaseReference storeNickName(String userId, String nickName) {
        DatabaseReference userRef = retrieveUser(userId);
        userRef.child(NICK_NAME_KEY).setValue(nickName);

        return userRef.child(NICK_NAME_KEY);
    }

    public DatabaseReference storeUserDate(String userId, String date) {
        DatabaseReference userRef = retrieveUser(userId);
        userRef.child(USER_DATE_KEY).setValue(date);

        return userRef.child(USER_DATE_KEY);
    }

    public DatabaseReference storeUserTime(String userId, String time) {
        DatabaseReference userRef = retrieveUser(userId);
        userRef.child(USER_TIME_KEY).setValue(time);

        return userRef.child(USER_TIME_KEY);
    }

    public DatabaseReference getUsersDatabaseReference() {
        return mUsersDatabaseReference;
    }

    public FirebaseMessaging getFirebaseMessaging() {
        return mFirebaseMessaging;
    }

    public DatabaseReference retrieveShareUrl() {
        return mRoot.child(SHARE_URL_KEY);
    }

    public DatabaseReference retrieveSurveyUrl() {
        return mRoot.child(SURVEY_URL_KEY);
    }

    private void subscribeToAnkimals() {
        mFirebaseMessaging.subscribeToTopic("ankimals");

    }
}
