package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.OakClientClient;
import me.leeeaf.oakclient.systems.commands.Command;
import me.leeeaf.oakclient.utils.io.ChatLogger;
import net.minecraft.text.Text;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class FOVCommand extends Command {
    public FOVCommand() {
        super("fov", "Changes camera FOV", new String[]{"FOV"}, null);
    }


    @Override
    public void execute(String[] args) {
        try{
            int fov = Integer.parseInt(args[0]);
            if(fov >= 30 && fov <= 110) {
                mc.options.getFov().setValue(fov);
            }
            else{
                ChatLogger.error(Text.of("FOV value must be between 30 and 110"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
