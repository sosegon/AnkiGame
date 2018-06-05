package com.ichi2.anki.ankigame.features.leaderboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ichi2.anki.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lbl_player_name)
        public TextView lblPlayerName;

        @BindView(R.id.lbl_player_points)
        public TextView lblPlayerPoints;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public LeaderboardAdapter(LayoutInflater layoutInflater, Context contex) {
        mLayoutInflater = layoutInflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.lblPlayerName.setText("");
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
