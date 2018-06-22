package com.ichi2.anki.ankigame.features.reviewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.ichi2.anki.BuildConfig;
import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.AnkiLog;
import com.ichi2.anki.ankigame.injection.ApplicationContext;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;

import javax.inject.Inject;

import timber.log.Timber;

import static com.ichi2.anki.AbstractFlashcardViewer.EASE_1;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_2;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_3;
import static com.ichi2.anki.AbstractFlashcardViewer.EASE_4;

@ConfigPersistent
public class ReviewerPresenter extends BasePresenter<ReviewerMvpView> {
    private static final String LOG_TAG = ReviewerPresenter.class.getSimpleName();

    private Context mContext;
    private final DataManager mDataManager;
    private String mDeckInfo;
    private String mDueDeckInfo;
    private boolean mIsFavCard;
    private String mCardInfo;
    private String mCardAnswer;
    // Used to record time between watching a question and answering it
    // Also used to record time between answering a question and assesing it
    private long mElapsedTime;
    private int mCoinsInCard;
    private int mPointsInCard;
    private int mCardEase;

    private int mElapsedTimeToAnswer;
    int mZeroRange = 1; // To avoid cheating due to fast assessing of cards
    int mIncreaseRange = 3;

    private int mFreeAnimals;

    @Inject
    public ReviewerPresenter(@ApplicationContext Context context, DataManager dataManager) {
        mDataManager = dataManager;
        this.mContext = context;
        mFreeAnimals = countFreeAnimals();
    }

    public int getCoins() {
        return mDataManager.getPreferencesHelper().retrieveCoins();
    }

    public int getPoints() {
        return mDataManager.getPreferencesHelper().retrievePoints();
    }

    public String getNickName() {
        return mDataManager.getPreferencesHelper().retrieveNickName();
    }

    public String getShareUrl() {
        return mDataManager.getShareUrl();
    }

    public void increaseCoinsAndPoints(int ease) {
//        if(BuildConfig.DEBUG) {
//            Drawable draw = getAnimal(0);
//            getMvpView().showLiberatedAnimalMessage(draw);
//            return;
//        }
        int currentCoins = 0;

        // TODO: ANKIGAME, Double check the values
        switch (ease) {
            case EASE_1:
                currentCoins += 0;
                break;
            case EASE_2:
                currentCoins += 0;
                break;
            case EASE_3:
                currentCoins += 0;
                break;
            case EASE_4:
                currentCoins += 0;
                break;
        }

        int elapsedTime = (int)((System.currentTimeMillis() - mElapsedTime)/ 1000);

        // Add coins based on elapsed time
        int extraCoins = calcCoins((elapsedTime)) + calcCoins(mElapsedTimeToAnswer);
        currentCoins += extraCoins;
        Timber.d(LOG_TAG + ": elapsed time to answer: " + elapsedTime);
        Timber.d(LOG_TAG + ": coins based on time: " + extraCoins);

        int totalCoins = mDataManager.getPreferencesHelper().retrieveCoins();
        mDataManager.getPreferencesHelper().storeCoins(totalCoins + currentCoins);

        // Add points based on elapsed time
        int extraPoints = calcPoints(elapsedTime) + calcPoints(mElapsedTimeToAnswer);
        int totalPoints = mDataManager.getPreferencesHelper().retrievePoints();
        mDataManager.getPreferencesHelper().storePoints(totalPoints + extraPoints);

        increaseEarnedPoints(extraPoints);
        increaseEarnedCoins(extraCoins);

        mCoinsInCard = currentCoins;
        mPointsInCard = extraPoints;
        mCardEase = ease;

        int freeAnimals = countFreeAnimals();
        if(freeAnimals > mFreeAnimals) {
            Drawable icon = getAnimalIcon(freeAnimals - 1);
            getMvpView().showLiberatedAnimalMessage(icon);
            mFreeAnimals = freeAnimals;
        }
    }

    public void logSelectDeck() {
        AnkiLog ankiLog = AnkiLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setLogType(AnkiLog.TYPE_SELECT_DECK);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setDeckInfo(mDeckInfo);
        ankiLog.setDueDeckInfo(mDueDeckInfo);

        mDataManager.logBehaviour(ankiLog);
    }

    public void logDisplayAnswerCard() {
        AnkiLog ankiLog = AnkiLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setLogType(AnkiLog.TYPE_DISPLAY_ANSWER_CARD);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setCardAnswer(mCardAnswer);
        ankiLog.setCardInfo(mCardInfo);
        mElapsedTimeToAnswer = (int)((System.currentTimeMillis() - mElapsedTime)/ 1000);
        ankiLog.setElapsedTime((int)((System.currentTimeMillis() - mElapsedTime)/ 1000));
        ankiLog.setFavCard(mIsFavCard);
        ankiLog.setDeckInfo(mDeckInfo);
        ankiLog.setDueDeckInfo(mDueDeckInfo);

        mDataManager.logBehaviour(ankiLog);
    }

    public void logAssessCard() {
        AnkiLog ankiLog = AnkiLog.logBase(mDataManager.getPreferencesHelper().retrieveUserId());
        ankiLog.setLogType(AnkiLog.TYPE_ASSESS_CARD);
        ankiLog.setTotalCoins(getCoins());
        ankiLog.setTotalPoints(getPoints());
        ankiLog.setCoinsInCard(mCoinsInCard);
        ankiLog.setPointsInCard(mPointsInCard);
        ankiLog.setCardAnswer(mCardAnswer);
        ankiLog.setCardInfo(mCardInfo);
        ankiLog.setElapsedTime((int)((System.currentTimeMillis() - mElapsedTime)/ 1000));
        ankiLog.setCardEase(mCardEase);
        ankiLog.setFavCard(mIsFavCard);
        ankiLog.setDeckInfo(mDeckInfo);
        ankiLog.setDueDeckInfo(mDueDeckInfo);

        mDataManager.logBehaviour(ankiLog);
    }

    private int calcCoins(int elapsedTime) {
        if(elapsedTime <= mZeroRange) {
            return 0;
        } else if (elapsedTime > mZeroRange && elapsedTime <= mIncreaseRange) {
            return elapsedTime - mZeroRange;
        } else {
            return mIncreaseRange;
        }
    }

    private int calcPoints(int elapsedTime) {
        if(elapsedTime <= mZeroRange) {
            return 0;
        } else {
            return Math.max((int)(10 * Math.log10(elapsedTime)), 1);
        }
    }

    public void setDeckInfo(String mDeckInfo) {
        this.mDeckInfo = mDeckInfo;
    }

    public void setDueDeckInfo(String mDueDeckInfo) {
        this.mDueDeckInfo = mDueDeckInfo;
    }

    public void setElapsedTime(long mElapsedTime) {
        this.mElapsedTime = mElapsedTime;
    }

    public void setIsFavCard(boolean mIsFavCard) {
        this.mIsFavCard = mIsFavCard;
    }

    public void setCardInfo(String mCardInfo) {
        this.mCardInfo = mCardInfo;
    }

    public void setCardAnswer(String mCardAnswer) {
        this.mCardAnswer = mCardAnswer;
    }

    public void setCoinsInCard(int mCoinsInCard) {
        this.mCoinsInCard = mCoinsInCard;
    }

    public void setCardEase(int mCardEase) {
        this.mCardEase = mCardEase;
    }

    private void increaseEarnedPoints(int points) {
        int prev = mDataManager.getPreferencesHelper().retrieveEarnedPoints();
        mDataManager.getPreferencesHelper().storeEarnedPoints(prev + points);
    }

    private void increaseEarnedCoins(int coins) {
        int prev = mDataManager.getPreferencesHelper().retrieveEarnedCoins();
        mDataManager.getPreferencesHelper().storeEarnedCoins(prev + coins);
    }

    private int countFreeAnimals() {
        int[] pointsAch = mContext.getResources().getIntArray(R.array.achievement_values);
        int availablePoints = getPoints();

        int freeAnimals = 0;
        for(int points : pointsAch) {
            if(availablePoints >= points) {
                freeAnimals++;
            }
        }

        return freeAnimals;
    }

    private Drawable getAnimalIcon(int index){
        TypedArray iconAch = mContext.getResources().obtainTypedArray(R.array.achievements);

        try {
            return mContext.getResources().getDrawable(iconAch.getResourceId(index, -1));
        } catch (IndexOutOfBoundsException e) {
            Timber.e("Invalid achievement index");
        }

        // TODO: ANKIGAME, find a better icon
        return mContext.getResources().getDrawable(R.drawable.ic_block_32dp);
    }
}