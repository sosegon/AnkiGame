package com.ichi2.anki.ankigame.data.remote;

import android.os.Debug;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ichi2.anki.BuildConfig;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
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

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mLogsDatabaseReference;

    private FirebaseDatabase mFirebaseDatabase;

    public FirebaseHelper() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        DatabaseReference root;
        if(BuildConfig.FLAVOR == "connection") {
            root = mFirebaseDatabase.getReference().child(CONNECTION_KEY);
        } else {
            root = mFirebaseDatabase.getReference().child(INDEPENDENT_KEY);

        }
        mUsersDatabaseReference = root.child(USERS_KEY);
        mLogsDatabaseReference = root.child(LOGS_KEY);
    }

    public DatabaseReference storeUser(User user) {
        DatabaseReference userRef = mUsersDatabaseReference.push();
        userRef.setValue(user);
        return userRef;
    }

    public DatabaseReference storeLog(AnkiLog ankiLog) {
        DatabaseReference logRef = mLogsDatabaseReference.push();
        logRef.setValue(ankiLog);
        return logRef;
    }

    public DatabaseReference retrieveUser(String userId) {
        return mUsersDatabaseReference.child(userId);
    }

    public DatabaseReference storeBestScore(String userId, int bestScore) {
        DatabaseReference userRef = retrieveUser(userId);
        userRef.child(BEST_SCORE_KEY).setValue(bestScore);

        return userRef.child(BEST_SCORE_KEY);
    }
}
