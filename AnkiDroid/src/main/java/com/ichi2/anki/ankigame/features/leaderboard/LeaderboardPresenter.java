package com.ichi2.anki.ankigame.features.leaderboard;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.model.GameLog;
import com.ichi2.anki.ankigame.data.model.User;
import com.ichi2.anki.ankigame.injection.ApplicationContext;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;
import com.ichi2.anki.ankigame.util.AnkimalsUtils;

import javax.inject.Inject;

@ConfigPersistent
public class LeaderboardPresenter extends BasePresenter<LeaderboardMvpView> {

    private final DataManager mDataManager;
    private FirebaseRecyclerAdapter<User, PlayerViewHolder> mAdapter;
    private Context mContext;

    @Inject
    public LeaderboardPresenter(@ApplicationContext Context context, DataManager dataManager) {
        this.mContext = context;
        this.mDataManager= dataManager;
    }

    public void logCheckLeaderboard() {
        doLogCheckLeaderboard();
    }

    public FirebaseRecyclerAdapter<User, PlayerViewHolder> getAdapter() {
        return mAdapter;
    }

    public String getNickName() {
        return mDataManager.getPreferencesHelper().retrieveNickName();
    }

    public void updateNickName(String nickName) {
        mDataManager.getPreferencesHelper().storeNickName(nickName);
        mDataManager.getFirebaseHelper().storeNickName(getUserId(), nickName);
    }

    private String getUserId() {
        return mDataManager.getPreferencesHelper().retrieveUserId();
    }

    private void doLogCheckLeaderboard() {
        GameLog gameLog = GameLog.logBase(getUserId());
        gameLog.setLogType(GameLog.TYPE_CHECK_LEADERBOARD);
        gameLog.setTotalCoins(mDataManager.getPreferencesHelper().retrieveCoins());
        gameLog.setTotalPoints(mDataManager.getPreferencesHelper().retrievePoints());

        mDataManager.logBehaviour(gameLog);
    }

    public void initAdapter(View.OnClickListener mListenerChangeNickName) {

       Query query = mDataManager.getFirebaseHelper().getUsersDatabaseReference()
                        .orderByChild(User.PARAM_POINTS);

       mAdapter = new FirebaseRecyclerAdapter<User, PlayerViewHolder>(
               User.class,
               R.layout.leaderboard_item,
               PlayerViewHolder.class,
               query) {

           @Override
           protected void populateViewHolder(PlayerViewHolder viewHolder, User model, int position) {
               String nickName = model.getNickName();
               if(getRef(position).getKey().contentEquals(getUserId())) {
                   nickName = mContext.getResources().getString(R.string.edit) + nickName;
                   viewHolder.setOnClickListener(mListenerChangeNickName);
               }
               viewHolder.setNickName(nickName);
               viewHolder.setPoints(model.getPoints());

               int playerPosition = getItemCount() - position; // Since it is in reverse
               String sPlayerPosition = "";
               int rowColor = 0;
               if(playerPosition == 1) {
                   sPlayerPosition += mContext.getResources().getString(R.string.leaderboard_first);
                   rowColor = mContext.getResources().getColor(R.color.leaderboard_first);
               } else if(playerPosition == 2) {
                   sPlayerPosition += mContext.getResources().getString(R.string.leaderboard_second);
                   rowColor = mContext.getResources().getColor(R.color.leaderboard_second);
               } else if(playerPosition == 3) {
                   sPlayerPosition += mContext.getResources().getString(R.string.leaderboard_third);
                   rowColor = mContext.getResources().getColor(R.color.leaderboard_third);
               } else if(playerPosition == 4) {
                   sPlayerPosition += mContext.getResources().getString(R.string.leaderboard_fourth);
                   rowColor = mContext.getResources().getColor(R.color.leaderboard_fourth);
               } else if(playerPosition == 5) {
                   sPlayerPosition += mContext.getResources().getString(R.string.leaderboard_fifth);
                   rowColor = mContext.getResources().getColor(R.color.leaderboard_fifth);
               } else {
                   sPlayerPosition += mContext.getResources().getString(R.string.leaderboard_rest);
                   rowColor = mContext.getResources().getColor(R.color.leaderboard_rest);
               }

               viewHolder.setPosition(sPlayerPosition);
               viewHolder.setColor(rowColor);

               boolean ankimalColored = model.isColoredAnkimal();
               int ankimalIndex = model.getAnkimalIndex() - 1; // workaround for users with no avatar set
               Drawable avatar = AnkimalsUtils.getDrawableAnkimal(mContext, ankimalIndex, ankimalColored);
               viewHolder.setPlayerAvatar(avatar);
           }

           @Override
           public void onDataChanged() {
               super.onDataChanged();
               this.notifyDataSetChanged();
           }
       };
    }
}
