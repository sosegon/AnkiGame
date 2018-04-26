package com.ichi2.anki;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.ichi2.anim.ActivityTransitionAnimation;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class Game extends NavigationDrawerActivity {
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

    @BindView(R.id.game_menu)
    FloatingActionsMenu mFabGameMenu;

    @BindView(R.id.web_main)
    WebView mWebMain;

    // Set fullscreen toggle on webview LongClick
    @OnTouch(R.id.web_main)
    public boolean onTouchGameWebView(View v, MotionEvent event) {
        // Implement a long touch action by comparing
        // time between action up and action down
        long currentTime = System.currentTimeMillis();
        if ((event.getAction() == MotionEvent.ACTION_UP)
                && (Math.abs(currentTime - mLastTouch) > mTouchThreshold)) {
            boolean toggledFullScreen = !isFullScreen();
            saveFullScreen(toggledFullScreen);
            applyFullScreen(toggledFullScreen);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastTouch = currentTime;
        }
        // return so that the event isn't consumed but used
        // by the webview as well
        return false;
    }

    @SuppressLint({ "SetJavaScriptEnabled", "NewApi", "ShowToast" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initScreenSettings();

        setContentView(R.layout.game);

        ButterKnife.bind(this);

        initFabGameMenu();

        initWebMain(savedInstanceState);

        pressBackToast = Toast.makeText(getApplicationContext(), R.string.press_back_again_to_exit,
                Toast.LENGTH_SHORT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebMain.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private void initScreenSettings() {

        // Don't show an action bar or title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // If on android 3.0+ activate hardware acceleration
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        // Apply previous setting about showing status bar or not
        applyFullScreen(isFullScreen());

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
        WebSettings settings = mWebMain.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setDatabasePath(getFilesDir().getParentFile().getPath() + "/databases");

        // If there is a previous instance restore it in the webview
        if (savedInstanceState != null) {
            // TODO: If app was minimized and Locale language was changed, we need to reload webview with changed language
            mWebMain.restoreState(savedInstanceState);
        } else {
            // Load webview with current Locale language
            mWebMain.loadUrl("file:///android_asset/2048-react/index.html?lang=" + Locale.getDefault().getLanguage());
        }

        // TODO: AnkiGame, Remove this code if needed
        //Toast.makeText(getApplication(), R.string.toggle_fullscreen, Toast.LENGTH_SHORT).show();
    }

    private void initFabGameMenu() {
        if (mFabGameMenu != null) {
            mFabGameMenu.findViewById(R.id.fab_expand_menu_button).setContentDescription(getString(R.string.menu_options));
            configureFloatingActionsMenu();
        } else {
            // TODO: AnkiGame, Set button for versions of Android below v14
        }
    }

    private void configureFloatingActionsMenu() {
        final FloatingActionButton fabEarnCoinsButton = (FloatingActionButton) findViewById(R.id.fab_earn_coins_action);
        fabEarnCoinsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabGameMenu.collapse();
                optionsEarnCoins();
            }
        });
    }

    private void optionsEarnCoins() {
        Intent intent = new Intent(Game.this, DeckPicker.class);
        startActivityForResultWithAnimation(intent, GO_EARN_COINS, ActivityTransitionAnimation.RIGHT);
    }
}
