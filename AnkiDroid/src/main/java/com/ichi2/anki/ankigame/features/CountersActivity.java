package com.ichi2.anki.ankigame.features;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ichi2.anki.NavigationDrawerActivity;
import com.ichi2.anki.R;

public class CountersActivity extends NavigationDrawerActivity {
    TextSwitcher mLblCoins;
    TextSwitcher mLblPoints;
    TextView mLblPlayerName;
    TextSwitcher mLblAnkimals;
    ImageView mImvPlayerAnkimal;

    protected void initCounters(View maiView) {
        mLblCoins = maiView.findViewById(R.id.lbl_coins_game);
        mLblPoints = maiView.findViewById(R.id.lbl_points);
        mLblPlayerName = maiView.findViewById(R.id.lbl_player_name);
        mLblAnkimals = maiView.findViewById(R.id.lbl_ankimals);
        mImvPlayerAnkimal = maiView.findViewById(R.id.imv_player_ankimal);

        mLblPoints.setInAnimation(getBaseContext(), R.anim.slide_up_in);
        mLblPoints.setOutAnimation(getBaseContext(), R.anim.slide_up_out);
        mLblAnkimals.setInAnimation(getBaseContext(), R.anim.slide_up_in);
        mLblAnkimals.setOutAnimation(getBaseContext(), R.anim.slide_up_out);
    }

    public void updateLblGameCoins(int coins, boolean increase) {
        if(mLblCoins != null) {
            if(increase) {
                mLblCoins.setInAnimation(getBaseContext(), R.anim.slide_up_in);
                mLblCoins.setOutAnimation(getBaseContext(), R.anim.slide_up_out);
            } else {
                mLblCoins.setInAnimation(getBaseContext(), R.anim.slide_down_in);
                mLblCoins.setOutAnimation(getBaseContext(), R.anim.slide_down_out);
            }
            mLblCoins.setText(getString(R.string.coins, coins));
        }
    }

    public void updateLblPoints(int points) {
        if(mLblPoints != null) {
            mLblPoints.setText(getString(R.string.points, points));
        }
    }

    public void updateLblPlayerName(String name) {
        if(mLblPlayerName != null) {
            mLblPlayerName.setText(getString(R.string.nickname, name));
        }
    }

    public void updateLblAnkimals(int ankimals) {
        if(mLblAnkimals != null) {
            mLblAnkimals.setText(getString(R.string.ankimals, ankimals));
        }
    }

    public void updateImvPlayerAnkimal(Drawable icon) {
        if(mImvPlayerAnkimal != null) {
            ImageViewAnimatedChange(getBaseContext(), mImvPlayerAnkimal, icon);
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
                String shareUrl = getShareUrl();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message) + " " + shareUrl); // TODO: get prediction
                shareIntent.setType("text/plain");

                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_message)));
                return true;
            case R.id.act_privacy:
                openUrl(Uri.parse(getResources().getString(R.string.privacy_policy)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // To be implemented in every activity
    public String getShareUrl() {
        return "";
    }

    private void ImageViewAnimatedChange(Context c, final ImageView v, final Drawable new_image) {
        final Animation anim_in  = AnimationUtils.loadAnimation(c, R.anim.slide_up_in);
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.slide_up_out);
        anim_in.setDuration(150);
        anim_out.setDuration(150);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageDrawable(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

}
