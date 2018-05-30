package com.ichi2.anki.ankigame.data.remote;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.data.model.AppLog;
import com.ichi2.anki.ankigame.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Analytics {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    public Analytics(@ApplicationContext Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

}