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
    public static final String KEY_EARNED_COINS = "prf_earned_coins";
    public static final String KEY_EARNED_POINTS = "prf_earned_points";
    public static final String KEY_USER_ID = "prf_user_id";
    public static final String KEY_BEST_SCORE = "prf_best_score";
    public static final String KEY_NICK_NAME = "prf_nick_name";
    public static final String KEY_SHOW_RATER = "prf_show_rater";
    public static final String KEY_LAUNCHES = "prf_launches";
    public static final String KEY_FIRST_LAUNCH_DATE = "prf_first_launch_date";
    public static final String KEY_SURVEY_SCHEDULED = "prf_survey_scheduled";
    public static final String KEY_SURVEY_TAKEN = "prf_survey_taken";
    public static final String KEY_SCHEDULED_SURVEY_COUNT = "prf_scheduled_survey_count";

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
        return mPref.getInt(KEY_POINTS, 10);
    }

    public void storePoints(int points) {
        mPref.edit().putInt(KEY_POINTS, points).apply();
    }

    public int retrieveEarnedCoins() {
        return mPref.getInt(KEY_EARNED_COINS, 0);
    }

    public void storeEarnedCoins(int coins) {
        mPref.edit().putInt(KEY_EARNED_COINS, coins).apply();
    }

    public int retrieveEarnedPoints() {
        return mPref.getInt(KEY_EARNED_POINTS, 0);
    }

    public void storeEarnedPoints(int points) {
        mPref.edit().putInt(KEY_EARNED_POINTS, points).apply();
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

    public void storeShowRater(boolean show) {
        mPref.edit().putBoolean(KEY_SHOW_RATER, show).apply();
    }

    public boolean retrieveShowRater() {
        return mPref.getBoolean(KEY_SHOW_RATER, true);
    }

    public void storeLaunches(long launches) {
        mPref.edit().putLong(KEY_LAUNCHES, launches).apply();
    }

    public long retrieveLaunches() {
        return mPref.getLong(KEY_LAUNCHES, 0);
    }

    public void storeFirstLaunchDate(long date) {
        mPref.edit().putLong(KEY_FIRST_LAUNCH_DATE, date).apply();
    }

    public long retrieveFirstLaunchDate() {
        return mPref.getLong(KEY_FIRST_LAUNCH_DATE, 0);
    }

    public void storeSurveyScheduled(boolean scheduled) {
        mPref.edit().putBoolean(KEY_SURVEY_SCHEDULED, scheduled).apply();
    }

    public boolean retrieveSurveyScheduled() {
        return mPref.getBoolean(KEY_SURVEY_SCHEDULED, false);
    }

    public void storeSurveyTaken(boolean taken) {
        mPref.edit().putBoolean(KEY_SURVEY_TAKEN, taken).apply();
    }

    public boolean retrieveSurveyTaken() {
        return mPref.getBoolean(KEY_SURVEY_TAKEN, false);
    }

    public void storeScheduledSurveyCount(int count) {
        mPref.edit().putInt(KEY_SCHEDULED_SURVEY_COUNT, count);
    }

    public int retrieveScheduledSurveyCount() {
        return mPref.getInt(KEY_SCHEDULED_SURVEY_COUNT, 0);
    }
}
