package com.ichi2.anki.ankigame.features.leaderboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Leaderboard extends BaseDialogFragment implements LeaderboardMvpView {

    public static final String FRAGMENT_TAG = "leaderboard";

    @Inject
    LeaderboardPresenter mPresenter;

    @BindView(R.id.players)
    RecyclerView mPlayersList;

    @BindString(R.string.leaderboard)
    String sLeaderboard;

    public Leaderboard() {
    }

    // from https://javamidpoint.wordpress.com/2017/03/15/android-add-close-button-on-dialog-fragment-title/
    public static void addButtonToDialogTitle(Dialog mdialog) {
        TextView title = mdialog.findViewById(android.R.id.title);
        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_close_clear_cancel, 0);
        title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= title.getRight() - title.getTotalPaddingRight()) {
                        //mdialog.cancel();
                        mdialog.dismiss();

                        return true;
                    }
                }
                return true;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaderboard, container, false);

        ButterKnife.bind(this, view);

        activityComponent().inject(this);

        addButtonToDialogTitle(getDialog());
        getDialog().setTitle(sLeaderboard);

        mPresenter.attachView(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true); // reverse order

        mPlayersList.setLayoutManager(layoutManager);
        mPlayersList.setHasFixedSize(true); // Optimizations in the UI
        mPlayersList.setAdapter(mPresenter.getAdapter());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mPresenter.isViewAttached()) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter.isViewAttached()) {
            mPresenter.detachView();
        }
    }
}
