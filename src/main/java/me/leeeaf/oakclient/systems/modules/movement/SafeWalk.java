package me.leeeaf.oakclient.systems.modules.movement;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", "Prevents you from falling off blocks",()->true, true, Category.MOVEMENT);
        //logic handled in: ClientPlayerEntityMixin::clipAtLedge()
    }
}
