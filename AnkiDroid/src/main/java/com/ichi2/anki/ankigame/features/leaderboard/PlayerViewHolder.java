package com.ichi2.anki.ankigame.features.leaderboard;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ichi2.anki.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.lbl_player_name)
    public TextView lblPlayerName;

    @BindView(R.id.lbl_player_points)
    public TextView lblPlayerPoints;

    @BindView(R.id.lbl_player_position)
    public TextView lblPlayerPosition;

    @BindView(R.id.ll_player)
    public LinearLayout llPlayer;

    public PlayerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setNickName(String nickName) {
        lblPlayerName.setText(nickName);
    }

    public void setPoints(int points) {
        lblPlayerPoints.setText(String.valueOf(points) + "☆");
    }

    public void setPosition(String position) {
        lblPlayerPosition.setText(position);
    }

    public void setColor(int color){
        llPlayer.setBackgroundColor(color);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        lblPlayerName.setOnClickListener(listener);
    }

}
