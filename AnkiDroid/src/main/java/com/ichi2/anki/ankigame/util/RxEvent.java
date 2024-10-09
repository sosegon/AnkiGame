package com.ichi2.anki.ankigame.util;

public class RxEvent {

    private RX_EVENT_TYPE type;
    private Object argument;

    public RxEvent(RX_EVENT_TYPE type, Object argument) {
        this.type = type;
        this.argument = argument;
    }

    public RxEvent(RX_EVENT_TYPE type) {
        this.type = type;
    }

    public RX_EVENT_TYPE getType() {
        return type;
    }

    public Object getArgument() {
        return argument;
    }

    public enum RX_EVENT_TYPE {
        COINS_UPDATED
    }
}

