package me.leeeaf.oakclient.systems.modules.player;

import com.lukflug.panelstudio.base.IBoolean;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;

public class AntiWeb extends Module {
    public AntiWeb() {
        super("AntiWeb", "Removes slowdown from webs (vanilla only)", ()->true, true, Category.PLAYER);
        //logic handled in: CobwebBlockMixin
    }

}
