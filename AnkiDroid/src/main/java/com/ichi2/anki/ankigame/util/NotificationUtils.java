package com.ichi2.anki.ankigame.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.features.survey.Survey;

public class NotificationUtils {

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private static final int SURVEY_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int SURVEY_PENDING_INTENT_ID = 3417;
    /**
     * This notification channel id is used to link notifications to this channel
     */
    private static final String SURVEY_NOTIFICATION_CHANNEL_ID = "survey_notification_channel";

    public static void startSurvey(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    SURVEY_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(mChannel);
        }

        PendingIntent surveyIntent = contentIntent(context);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, SURVEY_NOTIFICATION_CHANNEL_ID)
                .setContentText(context.getString(R.string.study_end))
                .setSmallIcon(R.drawable.ic_stat_notify)
                .addAction(R.drawable.ic_stat_notify, context.getString(R.string.dialog_ok), surveyIntent)
                .setContentIntent(surveyIntent)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(SURVEY_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, Survey.class);
        return PendingIntent.getActivity(
                context,
                SURVEY_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

}