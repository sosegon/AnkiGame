package com.ichi2.game.injection.component;

import android.app.Application;
import android.content.Context;

import com.ichi2.anki.AnkiDroidApp;
import com.ichi2.game.data.DataManager;
import com.ichi2.game.data.local.PreferencesHelper;
import com.ichi2.game.injection.ApplicationContext;
import com.ichi2.game.injection.module.ApplicationModule;

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
}
