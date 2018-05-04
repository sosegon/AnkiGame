package com.ichi2.anki.ankigame.data.remote;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

@Singleton
public class FirebaseHelper {
    // Database
    public static final String USERS_KEY = "users";

    private FirebaseDatabase mFirebaseDatabase;

    public FirebaseHelper() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

}
