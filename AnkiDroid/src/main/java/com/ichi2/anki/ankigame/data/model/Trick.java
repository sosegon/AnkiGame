package com.ichi2.anki.ankigame.data.model;

public interface Trick {
    String TRICK_FAIL_BLOCKED = "trickBlocked"; // insufficient points
    String TRICK_FAIL_DISABLED = "trickDisabled"; // insufficient coins
    String TRICK_FAIL_NOT_PERMITTED= "trickNotPermitted"; // board state does not allow to execute
}
