package com.ichi2.anki.ankigame.features.customankimal;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BaseDialogFragment;
import com.ichi2.anki.ankigame.features.CountersActivity;
import com.ichi2.anki.ankigame.features.deckpicker.DeckPicker;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomAnkimal extends BaseDialogFragment implements CustomAnkimalMvpView {

    public static final String FRAGMENT_TAG = "customankimal";

    @Inject
    CustomAnkimalPresenter mPresenter;

    @BindView(R.id.rl_custom_ankimal)
    RelativeLayout rlCustomAnkimal;

    @BindView(R.id.imv_ankimal)
    ImageView imvAnkimal;

    @BindView(R.id.lbl_coins_select)
    TextSwitcher lblCoinsSelect;

    @BindView(R.id.ll_select)
    ViewGroup llSelect;

    @OnClick(R.id.ll_select)
    public void selectAnkimal() {
        mPresenter.selectAnkimal();
    }

    @BindView(R.id.lbl_coins_color)
    TextSwitcher lblCoinsColor;

    @BindView(R.id.ll_color)
    ViewGroup llColor;

    @OnClick(R.id.ll_color)
    public void colorAnkimal() {
        mPresenter.colorAnkimal();
    }

    public CustomAnkimal() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_ankimal, container, false);

        ButterKnife.bind(this, view);

        activityComponent().inject(this);

        // TODO: Ankigame enable logging
        //mPresenter.logCustomAnkimal();

        int ankimalIndex = getArguments().getInt("ankimalIndex");
        mPresenter.setAnkimalIndex(ankimalIndex);

        updateSelectViews();
        updateColorViews();
        updateAnkimalView();

        return view;
    }

    @Override
    public void showInsufficientCoinsMessage(int requiredCoins) {
        Snackbar snackbar = Snackbar.make(rlCustomAnkimal, getString(R.string.insufficient_coins, requiredCoins), Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
        if (tv != null) {
            tv.setTextColor(Color.WHITE);
            tv.setMaxLines(2);  // prevent tablets from truncating to 1 line
        }
        snackbar.show();
    }

    @Override
    public void updatePlayerIcon() {
        Activity iv = getActivity();

        if(iv instanceof CountersActivity) {
            ((CountersActivity)iv).updateImvPlayerAnkimal(mPresenter.getDrawableAnkimal());
        }
    }

    @Override
    public void updateAnkimalList() {
        Activity iv = getActivity();

        if(iv instanceof DeckPicker) {
            ((DeckPicker)iv).updateAnkimalList();
        }
    }

    @Override
    public void updateCoins() {
        Activity iv = getActivity();

        if(iv instanceof CountersActivity) {
            ((CountersActivity)iv).updateLblGameCoins(mPresenter.getCoins(), false);
        }
    }

    @Override
    public void updateSelectViews() {
        if(!mPresenter.isCurrentlySelected()) {
            llSelect.setEnabled(true);
            llSelect.setVisibility(View.VISIBLE);
        } else {
            llSelect.setEnabled(false);
            llSelect.setVisibility(View.GONE);
        }
        if(!mPresenter.wasPreviouslySelected() && !mPresenter.isCurrentlySelected()) {
            lblCoinsSelect.setText(String.valueOf(mPresenter.REQUIRED_COINS_TO_SELECT) + getString(R.string.coins_glyph));
            lblCoinsSelect.setVisibility(View.VISIBLE);
        } else {
            lblCoinsSelect.setText("");
            lblCoinsSelect.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateColorViews() {
        if(!mPresenter.isColored()) {
            llColor.setEnabled(true);
            llColor.setVisibility(View.VISIBLE);
            lblCoinsColor.setText(String.valueOf(mPresenter.REQUIRED_COINS_TO_COLOR) + getString(R.string.coins_glyph));
        } else {
            llColor.setEnabled(false);
            llColor.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateAnkimalView() {
        imvAnkimal.setImageDrawable(mPresenter.getDrawableAnkimal());
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
