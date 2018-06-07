package com.ichi2.anki.ankigame.features;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ichi2.anki.BuildConfig;
import com.ichi2.anki.NavigationDrawerActivity;
import com.ichi2.anki.R;

public class CountersActivity extends NavigationDrawerActivity {
    TextView mLblCoins;
    TextView mLblPoints;

    protected void initCounters(View maiView) {
        mLblCoins = maiView.findViewById(R.id.lbl_coins_game);
        mLblPoints = maiView.findViewById(R.id.lbl_points);

        if(BuildConfig.FLAVOR.contentEquals("independent")){
            mLblCoins.setVisibility(View.GONE);
        }
    }

    public void updateLblGameCoins(int coins) {
        if(mLblCoins != null) {
            mLblCoins.setText(getString(R.string.coins, coins));
        }
    }

    public void updateLblPoints(int points) {
        if(mLblPoints != null) {
            mLblPoints.setText(getString(R.string.points, points));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ankigame_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_about:
                View v = View.inflate(this, R.layout.about_ankigame, null);
                TextView tv = v.findViewById(R.id.txt_about);
                tv.setOnClickListener((view) -> {
                            openUrl(Uri.parse(getResources().getString(R.string.ankidroid_url)));
                        }
                );
                new MaterialDialog.Builder(this)
                        .title(R.string.about)
                        .positiveText(R.string.close)
                        .customView(v, true)
                        .show();
                return true;
            case R.id.act_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message)); // TODO: get prediction
                shareIntent.setType("text/plain");

                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_message)));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
