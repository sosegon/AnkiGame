package com.ichi2.anki.ankigame.features.deckpicker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.Achievement;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.injection.ApplicationContext;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;
import com.ichi2.anki.ankigame.services.SurveyReceiver;
import com.ichi2.anki.ankigame.util.AnkimalsUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

@ConfigPersistent
public class DeckPickerPresenter extends BasePresenter<DeckPickerMvpView> {
    private final static int DAYS_UNTIL_PROMPT = 15;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 20;//Min number of launches
    private final static String SURVEY_NOTIFICATION_ACTION = "SURVEY_NOTIFICATION_SERVICE";

    private final DataManager mDataManager;
    private Context mContext;
    private AchievementAdapter mAchievementAdapter;

    @Inject
    public DeckPickerPresenter(@ApplicationContext Context context, DataManager dataManager) {
        this.mContext = context;
        mDataManager = dataManager;
        updateAnimalList();
    }

    public AchievementAdapter getAchievementAdapter() {
        return mAchievementAdapter;
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
    }

    public int countFreeAnkimals() {
        return AnkimalsUtils.countFreeAnkimals(mContext, getPoints());
    }

    public String getNickName() {
        return mDataManager.getPreferencesHelper().retrieveNickName();
    }

    public String getShareUrl() {
        return mDataManager.getShareUrl();
    }

    public int getNumberFreeAnimals() {
        int[] pointsAch = mContext.getResources().getIntArray(R.array.achievement_values);
        int availablePoints = getPoints();

        int freeAnimals = 0;
        for(int points : pointsAch) {
            if(availablePoints > points) {
                freeAnimals++;
            }
        }

        return freeAnimals;
    }

    public void logGoToGame() {
        AnkiLog ankiLog = AnkiLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setLogType(AnkiLog.TYPE_GO_TO_GAME);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setEarnedPoints(getEarnedPoints());
        ankiLog.setEarnedCoins(getEarnedCoins());
        mDataManager.logBehaviour(ankiLog);
    }

    public String initUser() {
        return mDataManager.initUser();
    }

    public void addNewLaunch() {
        boolean showRater = mDataManager.getPreferencesHelper().retrieveShowRater();
        if(showRater == false) {
            return;
        }

        long launches = mDataManager.getPreferencesHelper().retrieveLaunches();
        mDataManager.getPreferencesHelper().storeLaunches(launches + 1);

        Long firstLaunchDate = mDataManager.getPreferencesHelper().retrieveFirstLaunchDate();
        if(firstLaunchDate == 0) {
            firstLaunchDate = System.currentTimeMillis();
            mDataManager.getPreferencesHelper().storeFirstLaunchDate(firstLaunchDate);
        }

        if(launches >= LAUNCHES_UNTIL_PROMPT) {
            if(System.currentTimeMillis() >= firstLaunchDate + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                getMvpView().showRateDialog();
            }
        }
    }

    public void avoidRater(){
        mDataManager.getPreferencesHelper().storeShowRater(false);
    }

    public String getAppPackageName(){
        return mContext.getPackageName();
    }

    public void scheduleSurveyNotification() {

        // Survey is only available for users that used the app the ten days
        Long firstLaunchDate = mDataManager.getPreferencesHelper().retrieveFirstLaunchDate();
        Calendar firstWeekDate = Calendar.getInstance();
        firstWeekDate.set(Calendar.DAY_OF_MONTH, 28);
        firstWeekDate.set(Calendar.MONTH, Calendar.JUNE);
        firstWeekDate.set(Calendar.YEAR, 2018);

        if(firstLaunchDate > firstWeekDate.getTimeInMillis()) {
            return;
        }

        // Date for survey
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 9);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        // avoid scheduling the survey too many times
        int scheduledSurveyCount = mDataManager.getPreferencesHelper().retrieveScheduledSurveyCount();
        if(scheduledSurveyCount > 2) {
            return;
        }

        // Check if the survey has been taken and the date is past
        boolean surveyTaken = mDataManager.getPreferencesHelper().retrieveSurveyTaken();
        if(System.currentTimeMillis() > cal.getTimeInMillis() && !surveyTaken) {
            scheduleSurvey(System.currentTimeMillis());
            return;
        }

        // Check if the survey has been scheduled
        boolean scheduled = mDataManager.getPreferencesHelper().retrieveSurveyScheduled();
        if (scheduled) {
            return;
        }

        scheduleSurvey(cal.getTimeInMillis());
        mDataManager.getPreferencesHelper().storeSurveyScheduled(true);
    }

    public void updateAnimalList() {
        TypedArray iconAch = mContext.getResources().obtainTypedArray(R.array.achievements);
        int[] valueAch = mContext.getResources().getIntArray(R.array.achievement_values);

        int l = 0;
        if(iconAch.length() <= valueAch.length) {
            l = iconAch.length();
        } else {
            l = valueAch.length;
        }

        List<Achievement> achs = new ArrayList<>();
        for(int i = 0; i < l; i++) {
            Achievement a = new Achievement();
            int requiredPoints = valueAch[i];
            int currentPoints = getPoints();
            if(requiredPoints > currentPoints) {
                a.setAchievement(mContext.getResources().getDrawable(R.drawable.ic_block_32dp));
            } else {
                a.setAchievement(mContext.getResources().getDrawable(iconAch.getResourceId(i, -1)));
            }
            a.setPoints(requiredPoints);
            achs.add(a);
        }

        mAchievementAdapter = new AchievementAdapter(achs);
    }

    private void scheduleSurvey(long time) {
        Intent intent = new Intent(mContext, SurveyReceiver.class);
        intent.setAction(SURVEY_NOTIFICATION_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 001, intent, 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

        int scheduledSurveyCount = mDataManager.getPreferencesHelper().retrieveScheduledSurveyCount();
        mDataManager.getPreferencesHelper().storeScheduledSurveyCount(scheduledSurveyCount + 1);
    }

    private int getEarnedCoins() {
        return mDataManager.getPreferencesHelper().retrieveEarnedCoins();
    }

    private int getEarnedPoints() {
        return mDataManager.getPreferencesHelper().retrieveEarnedPoints();
    }
}
