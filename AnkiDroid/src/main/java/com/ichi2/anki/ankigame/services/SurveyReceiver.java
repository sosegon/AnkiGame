package com.ichi2.anki.ankigame.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ichi2.anki.ankigame.util.NotificationUtils;

public class SurveyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationUtils.startSurvey(context);
    }
}
