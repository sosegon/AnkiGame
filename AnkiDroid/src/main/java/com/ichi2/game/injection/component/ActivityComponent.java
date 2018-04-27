package com.ichi2.game.injection.component;

import com.ichi2.game.injection.PerActivity;
import com.ichi2.game.injection.module.ActivityModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
}
