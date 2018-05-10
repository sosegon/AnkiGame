package com.ichi2.anki.ankigame.data.remote;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.data.model.User;

import javax.inject.Singleton;

@Singleton
public class FirebaseHelper {
    // Database
    public static final String USERS_KEY = "users";
    public static final String LOGS_KEY = "logs";
    public static final String USERLOGS_KEY = "logs";

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mLogsDatabaseReference;

    private FirebaseDatabase mFirebaseDatabase;

    public FirebaseHelper() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(USERS_KEY);
        mLogsDatabaseReference = mFirebaseDatabase.getReference().child(LOGS_KEY);
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
}
