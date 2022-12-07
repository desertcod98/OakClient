package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.systems.commands.Command;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("reload", "Reloads the world renderer", new String[]{"reload", "reloadshaders", "reloadrenderer"}, null);
    }

    @Override
    public void excecute(String[] args) {
        mc.worldRenderer.reload();
    }
}
