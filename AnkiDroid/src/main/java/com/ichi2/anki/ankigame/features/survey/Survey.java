package com.ichi2.anki.ankigame.features.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.ichi2.anim.ActivityTransitionAnimation;
import com.ichi2.anki.AnkiActivity;
import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.features.deckpicker.DeckPicker;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Survey extends AnkiActivity implements SurveyMvpView{

    private static final int GO_DECK_PICKER= 130;

    @Inject
    SurveyPresenter mPresenter;

    @BindView(R.id.root_layout)
    ViewGroup rootLayout;

    @OnClick(R.id.start_survey)
    public void startSurvey() {
        openUrl(mPresenter.getSurveyUrl());
        mPresenter.surveyTaken();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent().inject(this);

        setContentView(R.layout.survey);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) rootLayout.findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mPresenter.tookSurvey()) {
            Intent intent = new Intent(Survey.this, DeckPicker.class);
            startActivityForResultWithAnimation(intent, GO_DECK_PICKER, ActivityTransitionAnimation.LEFT);
        }
    }
}
