package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.systems.commands.Command;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class ReloadSoundsCommand extends Command {
    public ReloadSoundsCommand() {
        super("ReloadSounds", "Reloads sound system to fix audio bugs", new String[]{"ReloadSounds", "SoundsReload"}, null);
    }

    @Override
    public void execute(String[] args) {
        mc.getSoundManager().reloadSounds();
    }
}
