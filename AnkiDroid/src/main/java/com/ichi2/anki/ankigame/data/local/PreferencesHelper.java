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
    public static final String KEY_POINTS = "prf_points";
    public static final String KEY_USER_ID = "prf_user_id";
    public static final String KEY_BEST_SCORE = "prf_best_score";
    public static final String KEY_NICK_NAME = "prf_nick_name";

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

    public void storeCoins(int coins) {
        mPref.edit().putInt(KEY_COINS, coins).apply();
    }

    public int retrievePoints() {
        return mPref.getInt(KEY_POINTS, 0);
    }

    public void storePoints(int points) {
        mPref.edit().putInt(KEY_POINTS, points).apply();
    }

    public String retrieveUserId() {
        return mPref.getString(KEY_USER_ID, "");
    }

    public void storeUserId(String userId) {
        mPref.edit().putString(KEY_USER_ID, userId).apply();
    }

    public void storeBestScore(int bestScore) {
        mPref.edit().putInt(KEY_BEST_SCORE, bestScore).apply();;
    }

    public int retrieveBestScore() {
        return mPref.getInt(KEY_BEST_SCORE, 0);
    }

    public void storeNickName(String nickname) {
        mPref.edit().putString(KEY_NICK_NAME, nickname).apply();
    }

    public String retrieveNickName() {
        return mPref.getString(KEY_NICK_NAME, "");
    }
}
