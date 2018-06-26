package com.ichi2.anki.ankigame.data.model;

import android.graphics.drawable.Drawable;

public class Achievement {
    private Drawable achievement;
    private int points;
    private boolean enabled;

    public Achievement() {
    }

    public Drawable getAchievement() {
        return achievement;
    }

    public void setAchievement(Drawable achievement) {
        this.achievement = achievement;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
