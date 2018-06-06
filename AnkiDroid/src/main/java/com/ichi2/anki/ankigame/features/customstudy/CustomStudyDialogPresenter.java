package com.ichi2.anki.ankigame.features.customstudy;

import com.ichi2.anki.ankigame.base.BasePresenter;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class CustomStudyDialogPresenter extends BasePresenter<CustomStudyDialogMvpView> {
    private final DataManager mDataManager;

    @Inject
    public CustomStudyDialogPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }
}
