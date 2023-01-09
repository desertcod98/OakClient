package me.leeeaf.oakclient.event;

public enum EventPriority {
    LOWEST(1),
    LOW(2),
    NORMAL(3),
    HIGH(4),
    HIGHEST(5);

    private final int value;

    EventPriority(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
