package com.ichi2.anki.ankigame.features.deckpicker;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ichi2.anki.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AchievementViewHolver extends RecyclerView.ViewHolder{
    @BindView(R.id.lbl_achievement)
    public TextView lblAchievement;

    @BindView(R.id.lbl_points)
    public TextView lblPoints;

    @BindView((R.id.lbl_position))
    public TextView lblPosition;

    public AchievementViewHolver(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setPoints(int points) {
        lblPoints.setText(String.valueOf(points) + "★");
    }

    public void setAchievement(Drawable icon) {
        lblAchievement.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }

    public void setPosition(int position) {
        lblPosition.setText(String.valueOf(position));
    }
}
