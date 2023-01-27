package me.leeeaf.oakclient.event.events.io;

import me.leeeaf.oakclient.event.Event;
import me.leeeaf.oakclient.utils.io.KeyAction;

public class KeyEvent extends Event {
    public int key;
    public int modifiers;
    public KeyAction keyAction;

    public KeyEvent(int key, int modifiers, KeyAction keyAction) {
        this.key = key;
        this.modifiers = modifiers;
        this.keyAction = keyAction;
    }
}
