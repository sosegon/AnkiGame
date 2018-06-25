package com.ichi2.anki.ankigame.util;

import android.content.Context;

import com.ichi2.anki.R;

public class AnkimalsUtils {

    public static int countFreeAnkimals(Context context, int availablePoints) {
        int[] pointsAch = context.getResources().getIntArray(R.array.achievement_values);

        int freeAnkimals = 0;
        for(int points : pointsAch) {
            if(availablePoints >= points) {
                freeAnkimals++;
            }
        }

        return freeAnkimals;
    }
}
