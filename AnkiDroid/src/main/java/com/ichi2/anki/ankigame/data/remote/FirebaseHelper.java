package com.ichi2.anki.ankigame.data.remote;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    public static final String BEST_SCORE_KEY = "bestScore";
    public static final String POINTS_KEY = "points";
    public static final String NICK_NAME_KEY = "nickName";
    public static final String SHARE_URL_KEY = "shareUrl";
    public static final String SURVEY_URL_KEY = "surveyUrl";

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mLogsDatabaseReference;
    private DatabaseReference mRoot;
    private FirebaseDatabase mFirebaseDatabase;

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

    public DatabaseReference getUsersDatabaseReference() {
        return mUsersDatabaseReference;
    }

    public DatabaseReference retrieveShareUrl() {
        return mRoot.child(SHARE_URL_KEY);
    }

    public DatabaseReference retrieveSurveyUrl() {
        return mRoot.child(SURVEY_URL_KEY);
    }
}
