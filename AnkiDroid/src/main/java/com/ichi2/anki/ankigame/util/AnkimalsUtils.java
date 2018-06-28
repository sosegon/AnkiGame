package com.ichi2.anki.ankigame.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.data.DataManager;

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

    public static boolean isColoredAnkimal(DataManager dataManager, int ankimalIndex) {
        String sColoredAnkimals = getColoredAnkimals(dataManager);
        int[] iColoredAnkimals = convertAnkimals(sColoredAnkimals);

        if(iColoredAnkimals.length == 0) {
            return false;
        }

        return elementInArray(iColoredAnkimals, ankimalIndex);
    }

    public static Drawable getDrawableAnkimal(Context context, int ankimalIndex, boolean isColored) {
        TypedArray ankimalDrawables = context.getResources().obtainTypedArray(R.array.achievements);

        int totalAnkimals = ankimalDrawables.length();
        if(ankimalIndex < 0 || ankimalIndex >= totalAnkimals) {
            return context.getResources().getDrawable(R.drawable.ic_block_32dp);
        }

        assert ankimalIndex >= 0 && ankimalIndex < ankimalDrawables.length();

        android.graphics.drawable.Drawable ankimalDrawable = context.getResources().getDrawable(ankimalDrawables.getResourceId(ankimalIndex, -1));

        if(!isColored) {
            grayDrawable(ankimalDrawable);
        }

        return ankimalDrawable;
    }

    public static Drawable getPlayerDrawableAnkimal(Context context, DataManager dataManager) {
        int ankimalIndex = dataManager.getPreferencesHelper().retrieveLastSelectedAnkimal();
        int totalAnkimals = context.getResources().getIntArray(R.array.achievement_values).length;

        if(ankimalIndex < 0 || ankimalIndex >= totalAnkimals) {
            return context.getResources().getDrawable(R.drawable.ic_block_32dp);
        }

        boolean colored = AnkimalsUtils.isColoredAnkimal(dataManager, ankimalIndex);
        return AnkimalsUtils.getDrawableAnkimal(context, ankimalIndex, colored);
    }

    public static int[] convertAnkimals(String ankimals) {
        if(ankimals.contentEquals("")){
            return new int[0];
        }

        String[] sAnkimals = ankimals.split(",");
        int[] iAnkimals = new int[sAnkimals.length];
        for(int i = 0; i < sAnkimals.length; i++) {
            if(sAnkimals[i].contentEquals("")) {
                iAnkimals[i] = -1;
                continue;
            }
            iAnkimals[i] = Integer.valueOf(sAnkimals[i]);
        }

        return iAnkimals;
    }

    public static String getAnkimalName(Context context, int ankimalIndex) {
        return context.getResources().getStringArray(R.array.achievement_names)[ankimalIndex];
    }

    private static String getColoredAnkimals(DataManager dataManager) {
        return dataManager.getPreferencesHelper().retrieveColoredAnkimals();
    }

    private static boolean elementInArray(int[] array, int element) {
        for(int el : array) {
            if(el == element) {
                return true;
            }
        }

        return false;
    }

    private static void grayDrawable(Drawable icon) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        icon.setColorFilter(filter);
    }
}
