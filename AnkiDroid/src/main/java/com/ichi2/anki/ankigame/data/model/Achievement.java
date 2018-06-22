package com.ichi2.anki.ankigame.data.model;

import android.graphics.drawable.Drawable;

public class Achievement {
    private Drawable achievement;
    private int points;

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
}
