package com.ichi2.anki.ankigame.data;

import com.google.firebase.database.DatabaseReference;
import com.ichi2.anki.ankigame.data.local.PreferencesHelper;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.data.model.User;
import com.ichi2.anki.ankigame.data.remote.FirebaseHelper;
import com.ichi2.anki.ankigame.util.RxEventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

import static com.ichi2.anki.ankigame.data.remote.FirebaseHelper.USERLOGS_KEY;

@Singleton
public class DataManager {

    private static final String LOG_TAG = DataManager.class.getSimpleName();
    private final PreferencesHelper mPreferencesHelper;
    private final FirebaseHelper mFirebaseHelper;
    private final RxEventBus mEventBus;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, RxEventBus eventBus) {
        mPreferencesHelper = preferencesHelper;
        mFirebaseHelper = new FirebaseHelper(); // Does not depend on anything
        mEventBus = eventBus;
        initBus();
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public FirebaseHelper getFirebaseHelper() {
        return mFirebaseHelper;
    }

    public String initUser() {
        String userId = mPreferencesHelper.retrieveUserId();

        // Create new user if necessary
        if(userId.equals("")) {
            DatabaseReference userRef = mFirebaseHelper.storeUser(new User(""));

            // Store the key locally
            userId = userRef.getKey();
            mPreferencesHelper.storeUserId(userId);
        }

        return userId;
    }

    public void logBehaviour(int logType) {
        AnkiLog log = null;
        switch (logType) {
            case AnkiLog.START_GAME:
                log = logStartGame();
                break;
            case AnkiLog.END_GAME:
                log = logEndGame();
                break;
            case AnkiLog.USE_TRICK:
                log = logUseTrick();
                break;
            case AnkiLog.SELECT_GAME_MODE:
                log = logSelectGame();
                break;
            case AnkiLog.CHECK_LEADERBOARD:
                log = logCheckLeaderboard();
                break;
            case AnkiLog.RESTART_GAME:
                log = logRestartGame();
                break;
            case AnkiLog.GO_TO_ANKI:
                log = logGoToAnki();
                break;
            case AnkiLog.GO_TO_GAME:
                log = logGoToGame();
                break;
            case AnkiLog.TAKE_QUIZZ:
                log = logTakeQuizz();
                break;
            default:
                break;
        }

        if(log != null) {
            DatabaseReference logRef = mFirebaseHelper.storeLog(log);

            // Store the reference of the log under user record
            DatabaseReference userRef = mFirebaseHelper.retrieveUser(log.getUserId());
            userRef.child(USERLOGS_KEY).child(logRef.getKey()).setValue(true);
        }
    }

    private void initBus() {
        // Subscribe consumers
    }

    private AnkiLog logBase() {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String sDate = dateFormat.format(newDate);
        String sTime = timeFormat.format(newDate);

        String userId = getPreferencesHelper().retrieveUserId();

        return new AnkiLog(userId, sDate, sTime);
    }

    private AnkiLog logStartGame() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setStartGame();

        return log;
    }

    private AnkiLog logEndGame() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setEndGame();

        return log;
    }

    private AnkiLog logUseTrick() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setUseTrick();

        return log;
    }

    private AnkiLog logSelectGame() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setSelectGame();

        return log;
    }

    private AnkiLog logCheckLeaderboard() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setCheckLeaderboard();

        return log;
    }

    private AnkiLog logRestartGame() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setRestartGame();

        return log;
    }

    private AnkiLog logGoToAnki() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setGoToAnki();

        return log;
    }

    private AnkiLog logGoToGame() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setGoToGame();

        return log;
    }

    private AnkiLog logTakeQuizz() {
        AnkiLog log = logBase();

        // TODO: AnkiGame, complete code to use function below
        // log.setTakeQuizz();

        return log;
    }
}
