package com.ichi2.anki.ankigame.data.remote;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.data.model.AppLog;
import com.ichi2.anki.ankigame.data.model.GameLog;
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

    public void logEvent(AppLog appLog) {
        Bundle params;
        if(appLog.getLogType().contentEquals(AnkiLog.TYPE_GO_TO_GAME)){
            params = logGoToGame(appLog);
        } else if(appLog.getLogType().contentEquals(AnkiLog.TYPE_SELECT_DECK)){
            params = logSelectDeck(appLog);
        } else if(appLog.getLogType().contentEquals(AnkiLog.TYPE_DISPLAY_ANSWER_CARD)){
            params = logDisplayAnswerCard(appLog);
        } else if(appLog.getLogType().contentEquals(AnkiLog.TYPE_ASSESS_CARD)){
            params = logAssessCard(appLog);
        } else if(appLog.getLogType().contentEquals(AnkiLog.TYPE_CUSTOM_STUDY)){
            params = logCustomStudy(appLog);
        } else if(appLog.getLogType().contentEquals(GameLog.TYPE_USE_TRICK)){
            params = logUseTrick(appLog);
        } else if(appLog.getLogType().contentEquals(GameLog.TYPE_FAIL_TRICK)){
            params = logFailTrick(appLog);
        } else if(appLog.getLogType().contentEquals(GameLog.TYPE_RESTART_GAME)){
            params = logRestartGame(appLog);
        } else if(appLog.getLogType().contentEquals(GameLog.TYPE_LOST_GAME)){
            params = logLostGame(appLog);
        } else if(appLog.getLogType().contentEquals(GameLog.TYPE_WON_GAME)){
            params = logWonGame(appLog);
        } else if(appLog.getLogType().contentEquals(GameLog.TYPE_GO_TO_ANKI)){
            params = logGoToAnki(appLog);
        } else if(appLog.getLogType().contentEquals(GameLog.TYPE_CHECK_LEADERBOARD)){
            params = logCheckLeaderboard(appLog);
        } else {
            return;
        }

        mFirebaseAnalytics.logEvent(appLog.getLogType(), params);
    }

    private Bundle logGoToGame(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);
        // TODO: AnkiGame, add logic to logEvent the earned coins as well.

        return params;
    }

    private Bundle logSelectDeck(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof AnkiLog){
            // TODO: Process info from due deck to avoid missing it
            // Do it in the log class. Max number of chars in event value is 100
            params.putString(AnkiLog.PARAM_DECK_INFO, ((AnkiLog)appLog).getDeckInfo());
            params.putString(AnkiLog.PARAM_DUE_DECK_INFO, ((AnkiLog)appLog).getDueDeckInfo());
        }

        return params;
    }

    private Bundle logDisplayAnswerCard(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof AnkiLog){
            params.putString(AnkiLog.PARAM_CARD_ANSWER, ((AnkiLog)appLog).getCardAnswer());
            params.putString(AnkiLog.PARAM_CARD_INFO, ((AnkiLog)appLog).getCardInfo());
            params.putInt(AnkiLog.PARAM_ELAPSED_TIME, ((AnkiLog)appLog).getElapsedTime());
            params.putBoolean(AnkiLog.PARAM_IS_FAV_CARD, ((AnkiLog)appLog).isFavCard());
            params.putString(AnkiLog.PARAM_DECK_INFO, ((AnkiLog)appLog).getDeckInfo());
            params.putString(AnkiLog.PARAM_DUE_DECK_INFO, ((AnkiLog)appLog).getDueDeckInfo());
        }

        return params;
    }

    private Bundle logAssessCard(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof AnkiLog){
            params.putInt(AnkiLog.PARAM_COINS_IN_CARD, ((AnkiLog)appLog).getCoinsInCard());
            params.putString(AnkiLog.PARAM_CARD_ANSWER, ((AnkiLog)appLog).getCardAnswer());
            params.putString(AnkiLog.PARAM_CARD_INFO, ((AnkiLog)appLog).getCardInfo());
            params.putInt(AnkiLog.PARAM_ELAPSED_TIME, ((AnkiLog)appLog).getElapsedTime());
            params.putInt(AnkiLog.PARAM_CARD_EASE, ((AnkiLog)appLog).getCardEase());
            params.putBoolean(AnkiLog.PARAM_IS_FAV_CARD, ((AnkiLog)appLog).isFavCard());
            params.putString(AnkiLog.PARAM_DECK_INFO, ((AnkiLog)appLog).getDeckInfo());
            params.putString(AnkiLog.PARAM_DUE_DECK_INFO, ((AnkiLog)appLog).getDueDeckInfo());
        }

        return params;
    }

    private Bundle logCustomStudy(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof AnkiLog){
            // TODO: Process info from due deck to avoid missing it
            // Do it in the log class. Max number of chars in event value is 100
            params.putString(AnkiLog.PARAM_CUSTOM_STUDY_OPTION, ((AnkiLog)appLog).getCustomStudyOption());
            params.putString(AnkiLog.PARAM_DECK_INFO, ((AnkiLog)appLog).getDeckInfo());
        }

        return params;
    }


    private Bundle logUseTrick(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof GameLog) {
            params.putInt(GameLog.PARAM_BEST_SCORE, ((GameLog)appLog).getBestScore());
            params.putInt(GameLog.PARAM_CURRENT_SCORE, ((GameLog)appLog).getCurrentScore());
            params.putString(GameLog.PARAM_USED_TRICKS, ((GameLog)appLog).getUsedTricks());
            params.putString(GameLog.PARAM_BOARD_VALUES, ((GameLog)appLog).getBoardValues());
            params.putString(GameLog.PARAM_TRICK_TYPE, ((GameLog)appLog).getTrickType());
        }

        return params;
    }

    private Bundle logFailTrick(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof GameLog) {
            params.putInt(GameLog.PARAM_BEST_SCORE, ((GameLog)appLog).getBestScore());
            params.putInt(GameLog.PARAM_CURRENT_SCORE, ((GameLog)appLog).getCurrentScore());
            params.putString(GameLog.PARAM_USED_TRICKS, ((GameLog)appLog).getUsedTricks());
            params.putString(GameLog.PARAM_BOARD_VALUES, ((GameLog)appLog).getBoardValues());
            params.putString(GameLog.PARAM_TRICK_TYPE, ((GameLog)appLog).getTrickType());
            params.putString(GameLog.PARAM_FAIL_TRICK_TYPE, ((GameLog)appLog).getFailTrickType());
        }

        return params;
    }

    private Bundle logRestartGame(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof GameLog) {
            params.putInt(GameLog.PARAM_BEST_SCORE, ((GameLog)appLog).getBestScore());
            params.putInt(GameLog.PARAM_CURRENT_SCORE, ((GameLog)appLog).getCurrentScore());
            params.putString(GameLog.PARAM_USED_TRICKS, ((GameLog)appLog).getUsedTricks());
            params.putString(GameLog.PARAM_BOARD_VALUES, ((GameLog)appLog).getBoardValues());
        }

        return params;
    }

    private Bundle logLostGame(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof GameLog) {
            params.putInt(GameLog.PARAM_BEST_SCORE, ((GameLog)appLog).getBestScore());
            params.putInt(GameLog.PARAM_CURRENT_SCORE, ((GameLog)appLog).getCurrentScore());
            params.putString(GameLog.PARAM_USED_TRICKS, ((GameLog)appLog).getUsedTricks());
            params.putString(GameLog.PARAM_BOARD_VALUES, ((GameLog)appLog).getBoardValues());
        }

        return params;
    }

    private Bundle logWonGame(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof GameLog) {
            params.putInt(GameLog.PARAM_BEST_SCORE, ((GameLog)appLog).getBestScore());
            params.putInt(GameLog.PARAM_CURRENT_SCORE, ((GameLog)appLog).getCurrentScore());
            params.putString(GameLog.PARAM_USED_TRICKS, ((GameLog)appLog).getUsedTricks());
            params.putString(GameLog.PARAM_BOARD_VALUES, ((GameLog)appLog).getBoardValues());
        }

        return params;
    }

    private Bundle logGoToAnki(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        if(appLog instanceof GameLog) {
            params.putInt(GameLog.PARAM_BEST_SCORE, ((GameLog)appLog).getBestScore());
            params.putInt(GameLog.PARAM_CURRENT_SCORE, ((GameLog)appLog).getCurrentScore());
            params.putString(GameLog.PARAM_USED_TRICKS, ((GameLog)appLog).getUsedTricks());
            params.putString(GameLog.PARAM_BOARD_VALUES, ((GameLog)appLog).getBoardValues());
        }

        return params;
    }

    private Bundle logCheckLeaderboard(AppLog appLog) {
        Bundle params = getAppLogParams(appLog);

        return params;
    }

    private Bundle getAppLogParams(AppLog log) {
        Bundle params = new Bundle();
        params.putString(AppLog.PARAM_USER_ID, log.getUserId());
        params.putString(AppLog.PARAM_LOG_TYPE, log.getLogType());
        params.putString(AppLog.PARAM_DATE, log.getDate());
        params.putString(AppLog.PARAM_TIME, log.getTime());
        params.putInt(AppLog.PARAM_TOTAL_COINS, log.getTotalCoins());
        params.putInt(AppLog.PARAM_TOTAL_POINTS, log.getTotalPoints());

        return params;
    }

}