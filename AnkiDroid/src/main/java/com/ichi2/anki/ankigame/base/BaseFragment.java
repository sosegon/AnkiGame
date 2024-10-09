package com.ichi2.anki.ankigame.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ichi2.anki.AnkiDroidApp;
import com.ichi2.anki.ankigame.injection.component.ActivityComponent;
import com.ichi2.anki.ankigame.injection.component.ConfigPersistentComponent;
import com.ichi2.anki.ankigame.injection.component.DaggerConfigPersistentComponent;
import com.ichi2.anki.ankigame.injection.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

public class BaseFragment extends Fragment {
    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private long mFragmentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mFragmentId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_FRAGMENT_ID) :
                NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent;

        if (!sComponentsMap.containsKey(mFragmentId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mFragmentId);

            configPersistentComponent = DaggerConfigPersistentComponent
                    .builder()
                    .applicationComponent(AnkiDroidApp.get(getContext()).getComponent())
                    .build();
            sComponentsMap.put(mFragmentId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mFragmentId);

            configPersistentComponent = sComponentsMap.get(mFragmentId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(getActivity()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_FRAGMENT_ID, mFragmentId);
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isChangingConfigurations()) {
            //Timber.i("Clearing ConfigPersistentComponent id=%d", mFragmentId);
            sComponentsMap.remove(mFragmentId);
        }
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

}
