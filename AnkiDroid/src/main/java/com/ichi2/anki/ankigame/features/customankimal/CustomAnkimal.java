package com.ichi2.anki.ankigame.features.customankimal;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ichi2.anki.R;
import com.ichi2.anki.ankigame.base.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomAnkimal extends BaseDialogFragment implements CustomAnkimalMvpView {

    public static final String FRAGMENT_TAG = "customankimal";

    @Inject
    CustomAnkimalPresenter mPresenter;

    @BindView(R.id.imv_ankimal)
    ImageView imvAnkimal;

    public CustomAnkimal() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_ankimal, container, false);

        ButterKnife.bind(this, view);

        activityComponent().inject(this);

        //mPresenter.logCustomAnkimal();

        TypedArray grayIconAch = getResources().obtainTypedArray(R.array.achievements);
        int ankimalIndex = getArguments().getInt("ankimalIndex");
        Drawable grayDrawable = getResources().getDrawable(grayIconAch.getResourceId(ankimalIndex, -1));
        imvAnkimal.setImageDrawable(grayDrawable);

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
