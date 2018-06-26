package com.ichi2.anki.ankigame.features.deckpicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.data.model.Achievement;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementViewHolver> {
    private List<Achievement> mAchievementList;
    private AnkimalClickListener mListener;

    public AchievementAdapter(List<Achievement>  achievementList, AnkimalClickListener listener) {
        mAchievementList = achievementList;
        mListener = listener;
    }

    @Override
    public AchievementViewHolver onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievements_item, parent, false);
        return new AchievementViewHolver(v);
    }

    @Override
    public void onBindViewHolder(AchievementViewHolver holder, int position) {
        Achievement ach = mAchievementList.get(position);
        holder.setPoints(ach.getPoints());
        holder.setAchievement(ach.getAchievement());
        holder.setPosition(position + 1);
        holder.setOnClickListener(mListener, position, ach.isEnabled());
    }

    @Override
    public int getItemCount() {
        return mAchievementList.size();
    }
}
