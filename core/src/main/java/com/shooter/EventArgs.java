package com.shooter;

public class EventArgs {
    public EventArgs(GameObject source) {
        this.gameObject = source;
    }

    final public GameObject gameObject;
}
