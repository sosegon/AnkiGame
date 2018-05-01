package com.ichi2.anki.ankigame.injection.component;

import android.app.Application;
import android.content.Context;

import com.ichi2.anki.AnkiDroidApp;
import com.ichi2.anki.ankigame.data.DataManager;
import com.ichi2.anki.ankigame.data.local.PreferencesHelper;
import com.ichi2.anki.ankigame.injection.ApplicationContext;
import com.ichi2.anki.ankigame.injection.module.ApplicationModule;
import com.ichi2.anki.ankigame.util.RxEventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(AnkiDroidApp ankiDroidApp);

    @ApplicationContext
    Context context();

    Application application();

    PreferencesHelper preferencesHelper();

    DataManager dataManager();

    RxEventBus eventBus();
}
