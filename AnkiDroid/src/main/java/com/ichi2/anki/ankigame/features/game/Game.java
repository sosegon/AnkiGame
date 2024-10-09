package com.ichi2.anki.ankigame.features.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.ichi2.anim.ActivityTransitionAnimation;
import com.ichi2.anki.R;
import com.ichi2.anki.UIUtils;
import com.ichi2.anki.ankigame.data.model.GameLog;
import com.ichi2.anki.ankigame.features.CountersActivity;
import com.ichi2.anki.ankigame.features.deckpicker.DeckPicker;
import com.ichi2.anki.ankigame.features.leaderboard.Leaderboard;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class Game extends CountersActivity implements GameMvpView {
    // TODO: AnkiGame, Change tag
    private static final String MAIN_ACTIVITY_TAG = "2048_MainActivity";

    /**
     * Available options performed by other activities (request codes for onActivityResult())
     */
    private static final int GO_EARN_COINS = 0;

    private long mLastBackPress;
    private static final long mBackPressThreshold = 3500;
    private static final String IS_FULLSCREEN_PREF = "is_fullscreen_pref";
    private static boolean DEF_FULLSCREEN = true;
    private long mLastTouch;
    private static final long mTouchThreshold = 2000;
    private Toast pressBackToast;

    @Inject GamePresenter mGamePresenter;

    Handler mHandler;

    TextView mLblCoins;

    @BindView(R.id.game_menu)
    FloatingActionsMenu mFabGameMenu;

    @BindView(R.id.fab_expand_menu_button)
    View mFabExpandMenuButton;

    @BindView(R.id.web_main)
    WebView mWebMain;

    @BindView(R.id.root_layout)
    ViewGroup rootLayout;

    @BindView(R.id.lbl_message)
    TextView mMessage;

    // Set fullscreen toggle on webview LongClick
    @OnTouch(R.id.web_main)
    public boolean onTouchGameWebView(View v, MotionEvent event) {
        // Implement a long touch action by comparing
        // time between action up and action down
        long currentTime = System.currentTimeMillis();
        if ((event.getAction() == MotionEvent.ACTION_UP)
                && (Math.abs(currentTime - mLastTouch) > mTouchThreshold)) {
            boolean toggledFullScreen = !isFullScreen();
            //saveFullScreen(toggledFullScreen);
            //applyFullScreen(toggledFullScreen);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastTouch = currentTime;
        } else if(event.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }
        // return so that the event isn't consumed but used
        // by the webview as well
        return false;
    }

    @OnClick(R.id.fab_earn_coins_action)
    public void earnCoins() {
        if (mFabGameMenu == null) {
            return;
        }
        mFabGameMenu.collapse();
        Intent intent = new Intent(Game.this, DeckPicker.class);
        String logCode = GameLog.TYPE_GO_TO_ANKI;
        mWebMain.loadUrl("javascript:goToAnki(\"" + logCode + "\")");
        finishWithAnimation(ActivityTransitionAnimation.DOWN);
        startActivityForResultWithAnimation(intent, GO_EARN_COINS, ActivityTransitionAnimation.RIGHT);
    }

    @OnClick(R.id.fab_restart_action)
    public void restartGame() {
        if (mFabGameMenu == null) {
            return;
        }
        mFabGameMenu.collapse();

        String logCode = GameLog.TYPE_RESTART_GAME;

        new MaterialDialog.Builder(this)
        .title(sRestart)
        .positiveText(R.string.dialog_ok)
        .onPositive(
                (dialog, which) ->  mWebMain.loadUrl("javascript:restartGame(\"" + logCode + "\")")
        )
        .negativeText(R.string.dialog_cancel)
        .onNegative(
                (dialog, which) ->  {}
        )
        .show();
    }

    @OnClick(R.id.fab_leaderboard_action)
    public void showLeaderboard() {
        if (mFabGameMenu == null) {
            return;
        }
        mFabGameMenu.collapse();

        Leaderboard leaderboard = new Leaderboard();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(Leaderboard.FRAGMENT_TAG);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        leaderboard.show(ft, Leaderboard.FRAGMENT_TAG);
    }

    @OnClick(R.id.fab_howto_action)
    public void showHelp() {
        if (mFabGameMenu == null) {
            return;
        }
        mFabGameMenu.collapse();

        new MaterialDialog.Builder(Game.this)
                .title(R.string.how_to_play)
                .positiveText(R.string.close)
                .customView(R.layout.how_to_play, true)
                .show();
    }

    @BindString(R.string.press_back_again_to_exit)
    String sBack;

    @BindString(R.string.menu_options)
    String sMenuOptions;

    @BindString(R.string.restart_game)
    String sRestart;

    @BindString(R.string.has_lost_game)
    String sHasLost;

    @BindString(R.string.has_won_game)
    String sHasWon;

    @SuppressLint({ "SetJavaScriptEnabled", "NewApi", "ShowToast" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent().inject(this);

        //initScreenSettings();

        setContentView(R.layout.game);

        ButterKnife.bind(this);

        mHandler = new Handler();

        mGamePresenter.attachView(this);

        initCounters(rootLayout);

        Toolbar toolbar = (Toolbar) rootLayout.findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        initFabGameMenu();

        initWebMain(savedInstanceState);

        updateImvPlayerAnkimal(mGamePresenter.getPlayerDrawableAnkimal()); // update here not in onResume

        pressBackToast = Toast.makeText(getApplicationContext(), sBack, Toast.LENGTH_SHORT);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mGamePresenter.isViewAttached()) {
            mGamePresenter.attachView(this);
        }
        // This is specially useful when arriving from DeckPicker
        mGamePresenter.resetEarnedCoins();
        mGamePresenter.resetEarnedPoints();

        updateLblGameCoins(mGamePresenter.getCoins(), true);
        updateLblPoints(mGamePresenter.getPoints());
        updateLblAnkimals(mGamePresenter.countFreeAnkimals());
        updateLblPlayerName(mGamePresenter.getNickName());
        // To update the visual of the tricks based on the coins and points
        mWebMain.loadUrl("file:///android_asset/2048-react/index.html?lang=" + Locale.getDefault().getLanguage());
        updateWebMainVisual();
        mMessage.setText(R.string.loading);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove the web page to avoid wrong state when coming back to the activity
        // This is because the visual of the page depends on the coins and points
        mWebMain.loadUrl("about:blank");

        mMessage.setText(R.string.pausing);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGamePresenter.isViewAttached()) {
            mGamePresenter.detachView();
        }
    }

    @Override
    public void postRunnable(Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * Saves the full screen setting in the SharedPreferences
     * @param isFullScreen
     */

    private void saveFullScreen(boolean isFullScreen) {
        // save in preferences
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(IS_FULLSCREEN_PREF, isFullScreen);
        editor.commit();
    }

    private boolean isFullScreen() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(IS_FULLSCREEN_PREF,
                DEF_FULLSCREEN);
    }

    /**
     * Toggles the activitys fullscreen mode by setting the corresponding window flag
     * @param isFullScreen
     */
    private void applyFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Prevents app from closing on pressing back button accidentally.
     * mBackPressThreshold specifies the maximum delay (ms) between two consecutive backpress to
     * quit the app.
     */
    // TODO: AnkiGame, Check this function when adding actions menu
    // TODO: AnkiGame, See the flow between Game and DeckPicker
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - mLastBackPress) > mBackPressThreshold) {
            pressBackToast.show();
            mLastBackPress = currentTime;
        } else {
            pressBackToast.cancel();
            super.finishWithAnimation(ActivityTransitionAnimation.DOWN);
        }
    }

    @Override
    public void showInsufficientCoinsMessage(int requiredCoins) {
        UIUtils.showSnackbar(
                this,
                getString(R.string.insufficient_coins, requiredCoins),
                false,
                R.string.earn_coins,
                (view) -> earnCoins(),
                findViewById(R.id.root_layout),
                null
        );
    }

    @Override
    public void showUnableToDoTrickMessage(String trickName) {
        UIUtils.showSimpleSnackbar(
                this,
                getString(R.string.unable_trick, trickName),
                false
        );

    }

    @Override
    public void showInsufficientPointsMessage(int requiredPoints) {
        UIUtils.showSnackbar(
                this,
                getString(R.string.insufficient_points, requiredPoints),
                false,
                R.string.earn_points,
                (view) -> earnCoins(),
                findViewById(R.id.root_layout),
                null
        );
    }

    @Override
    public void showHasLostDialog() {
        String logCode = GameLog.TYPE_LOST_GAME;

        new MaterialDialog.Builder(this)
                .title(sHasLost)
                .positiveText(R.string.earn_coins)
                .negativeText(sRestart)
                .neutralText(R.string.close)
                .onPositive(
                        (dialog, which) ->  earnCoins()
                )
                .onNegative(
                        (dialog, which) ->  mWebMain.loadUrl("javascript:restartGame(\"" + logCode + "\")")
                )
                .onNeutral(
                        (dialog, which) ->  {}
                )
                .show();
    }

    @Override
    public void showHasWonDialog() {
        String logCodeRestart = GameLog.TYPE_RESTART_GAME;

        new MaterialDialog.Builder(Game.this)
                .title(sHasWon)
                .positiveText(R.string.dialog_continue)
                .negativeText(R.string.restart_game)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mWebMain.loadUrl("javascript:continuePlaying()");
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        mWebMain.loadUrl("javascript:restartGame(\"" + logCodeRestart + "\")");
                    }
                })

                .show();
    }

    // ANKIGAME
    @Override
    public String getShareUrl() {
        return mGamePresenter.getShareUrl();
    }

    private void initScreenSettings() {

        // Don't show an action bar or title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // If on android 3.0+ activate hardware acceleration
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        // Apply previous setting about showing status bar or not
        //applyFullScreen(isFullScreen());

        // Check if screen rotation is locked in settings
        boolean isOrientationEnabled = false;
        try {
            isOrientationEnabled = Settings.System.getInt(getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION) == 1;
        } catch (Settings.SettingNotFoundException e) {
            Log.d(MAIN_ACTIVITY_TAG, "Settings could not be loaded");
        }

        // If rotation isn't locked and it's a LARGE screen then add orientation changes based on sensor
        int screenLayout = getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (((screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE)
                || (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE))
                && isOrientationEnabled) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    private void initWebMain(Bundle savedInstanceState) {
        // TODO: AnkiGame, Remove this code if needed
        /*ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }*/

        // Load webview with game
        // mWebMain = (WebView) findViewById(R.id.web_main);
        mWebMain.addJavascriptInterface(mGamePresenter, "Anki");
        WebSettings settings = mWebMain.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setDatabasePath(getFilesDir().getParentFile().getPath() + "/databases");
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        // If there is a previous instance restore it in the webview
        /*if (savedInstanceState != null) {
            // TODO: If app was minimized and Locale language was changed, we need to reload webview with changed language
            mWebMain.restoreState(savedInstanceState);
        } else {
            // Load webview with current Locale language
            mWebMain.loadUrl("file:///android_asset/2048-react/index.html?lang=" + Locale.getDefault().getLanguage());
        }*/

        // TODO: AnkiGame, Remove this code if needed
        //Toast.makeText(getApplication(), R.string.toggle_fullscreen, Toast.LENGTH_SHORT).show();
    }

    private void updateWebMainVisual() {
        // ANKIGAME hide scrollbars and blue shade
        mWebMain.setVerticalScrollBarEnabled(false);
        mWebMain.setHorizontalScrollBarEnabled(false);
        mWebMain.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mWebMain.setBackgroundColor(Color.argb(1, 0, 0, 0));

        mWebMain.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                float dpr = getResources().getDisplayMetrics().density;
                float width = view.getWidth() / dpr;
                float height = view.getHeight() / dpr;
                view.loadUrl("javascript:rescale("+ height + "," + width +")");
                super.onPageFinished(view, url);
            }
        });

    }

    private void initFabGameMenu() {
        if (mFabGameMenu != null) {
            mFabExpandMenuButton.setContentDescription(sMenuOptions);
        } else {
            // TODO: AnkiGame, Set button for versions of Android below v14
        }
    }
}
