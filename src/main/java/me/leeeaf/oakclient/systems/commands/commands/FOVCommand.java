package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.OakClientClient;
import me.leeeaf.oakclient.systems.commands.Command;
import net.minecraft.text.Text;

public class FOVCommand extends Command {
    public FOVCommand() {
        super("fov", "Changes camera FOV", new String[]{"FOV"});
    }


    @Override
    public void excecute(String[] args) {
        try{
            int fov = Integer.parseInt(args[0]);
            if(fov >= 30 && fov <= 110) {
                OakClientClient.mc.options.getFov().setValue(fov);
            }
            else{
                OakClientClient.mc.player.sendMessage(Text.of("FOV value must be between 30 and 110"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
