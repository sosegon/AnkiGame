package com.ichi2.anki.ankigame.features.leaderboard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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

        //addButtonToDialogTitle(getDialog());
        getDialog().setTitle(sLeaderboard);

        mPresenter.attachView(this);
        mPresenter.initAdapter(mListenerChangeNickName);
        mPresenter.updateUserRemotely(); // So, they user is updated in firebase
        mPresenter.logCheckLeaderboard();

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

    EditText mDialogEditText;

    View.OnClickListener mListenerChangeNickName = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mDialogEditText = new EditText(getActivity());

            int maxLength = 12;
            InputFilter[] filters = new InputFilter[1];
            filters[0] = new InputFilter.LengthFilter(maxLength);
            mDialogEditText.setFilters(filters);

            mDialogEditText.setSingleLine(true);
            mDialogEditText.setText(mPresenter.getNickName());

            new MaterialDialog.Builder(getActivity())
                    .title(R.string.update_nick_name)
                    .positiveText(R.string.dialog_ok)
                    .customView(mDialogEditText, true)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            String nickname = mDialogEditText.getText().toString();
                            mPresenter.updateNickName(nickname);

                            // Update the nickname in bar
                            TextView tv = getActivity().findViewById(R.id.lbl_player_name);
                            tv.setText(getString(R.string.nickname, nickname));
                        }
                    })
                    .negativeText(R.string.dialog_cancel)
                    .show();
        }
    };
}
