package com.ichi2.anki.ankigame.injection.component;

import com.ichi2.anki.ankigame.features.deckpicker.DeckPicker;
import com.ichi2.anki.ankigame.features.game.Game;
import com.ichi2.anki.ankigame.features.leaderboard.Leaderboard;
import com.ichi2.anki.ankigame.features.reviewer.Reviewer;
import com.ichi2.anki.ankigame.injection.PerActivity;
import com.ichi2.anki.ankigame.injection.module.ActivityModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(Game gameActivity);
    void inject(DeckPicker deckPickerActivity);
    void inject(Reviewer reviewerActivity);
    void inject(Leaderboard leaderboardFragment);
}
