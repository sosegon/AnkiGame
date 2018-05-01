package com.ichi2.anki.ankigame.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.ichi2.anki.ankigame.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {
    public static final String PREF_FILE_NAME = "ankigame_pref_file";

    public static final String KEY_COINS = "prf_coins";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public int retrieveCoins() {
        return mPref.getInt(KEY_COINS, 10);
    }

    /**
     *
     * @param coins positive or negative
     */
    public void updateCoins(int coins) {
        int current = retrieveCoins();
        mPref.edit().putInt(KEY_COINS, current+coins).apply();

    }
}
