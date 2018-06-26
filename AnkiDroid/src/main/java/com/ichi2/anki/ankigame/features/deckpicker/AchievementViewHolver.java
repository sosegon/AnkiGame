package com.ichi2.anki.ankigame.features.deckpicker;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichi2.anki.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AchievementViewHolver extends RecyclerView.ViewHolder{
    @BindView(R.id.imv_achievement)
    public ImageView imvAchievement;

    @BindView(R.id.lbl_points)
    public TextView lblPoints;

    @BindView((R.id.lbl_position))
    public TextView lblPosition;

    public AchievementViewHolver(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setPoints(int points) {
        lblPoints.setText(String.valueOf(points) + "☆");
    }

    public void setAchievement(Drawable icon) {
        imvAchievement.setImageDrawable(icon);
    }

    public void setPosition(int position) {
        lblPosition.setText(String.valueOf(position));
    }

    public void setOnClickListener(AnkimalClickListener listener, int ankimalIndex,  boolean enabled) {
        imvAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAnkimalSelected(ankimalIndex, enabled);
            }
        });
    }
}
