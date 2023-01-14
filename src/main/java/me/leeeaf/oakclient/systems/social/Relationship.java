package me.leeeaf.oakclient.systems.social;

import net.minecraft.util.Formatting;


public enum Relationship {
    FRIEND(Formatting.GREEN),
    ENEMY(Formatting.RED);

    private final Formatting color;

    Relationship(Formatting color){
        this.color = color;
    }

    public Formatting getColor() {
        return color;
    }
}
