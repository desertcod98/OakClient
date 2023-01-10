package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.systems.commands.Command;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.world.XRay;
import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;

import static me.leeeaf.oakclient.OakClientClient.mc;
import static me.leeeaf.oakclient.systems.modules.world.XRay.blocksToRender;

public class XrayCommand extends Command {
    Module xray;

    public XrayCommand() {

        //TODO modifying list reloads the world render even if xray is disabled
        super("xray", "Manages blocks to see with xray", new String[]{"xray", "xrayblocks"}, new Command[]{
                new Command("list", "show enabled targets", new String[]{"list"}, null) {
                    @Override
                    public void execute(String[] args) {
                        if(blocksToRender.size()>0){
                            mc.player.sendMessage(Text.literal("Targets:").formatted(Formatting.GREEN));
                            for(Block block: blocksToRender){
                                mc.player.sendMessage(Text.of(Registry.BLOCK.getId(block).toString()));
                            }
                        }else{
                            mc.player.sendMessage(Text.of("No targets in list! Add them with .xray add"));
                        }
                    }
                },
                new Command("add", "adds target", new String[]{"add"}, null) {
                    @Override
                    public void execute(String[] args) {
                        if(args.length>0){
                            Identifier blockToAddId = new Identifier(args[0]);
                            Block blockToAdd = Registry.BLOCK.get(blockToAddId);
                            if(!blockToAdd.equals(Registry.BLOCK.get(Registry.BLOCK.getDefaultId()))){
                                if(!blocksToRender.contains(blockToAdd)){
                                    blocksToRender.add(blockToAdd);
                                    mc.player.sendMessage(Text.literal("Added to xray targets: ").append(Text.literal(args[0]).formatted(Formatting.AQUA)));
                                }else{
                                    mc.player.sendMessage(Text.literal("Block already in list!"));
                                }

                            }else{
                                mc.player.sendMessage(Text.literal("Block not found!"));
                            }

                        }
                    }
                },
                new Command("remove", "removes target", new String[]{"remove"}, null) {
                    @Override
                    public void execute(String[] args) {
                        if(args.length>0){
                            Identifier blockToRemoveId = new Identifier(args[0]);
                            Block blockToRemove = Registry.BLOCK.get(blockToRemoveId);
                            if(blocksToRender.remove(blockToRemove)){
                                mc.player.sendMessage(Text.literal("Removed from xray targets: ").append(Text.literal(args[0]).formatted(Formatting.AQUA)));
                            }else{
                                mc.player.sendMessage(Text.literal("Block not found!"));
                            }
                        }
                    }
                },
                new Command("clear", "clears target list", new String[]{"clear"}, null) {
                    @Override
                    public void execute(String[] args) {
                        blocksToRender.clear();
                        mc.player.sendMessage(Text.of("Xray target list cleared!"));
                    }
                },
        });

        xray = Category.getModule(XRay.class);
    }

    @Override
    public void execute(String[] args) {
        if(args.length>0){
            for(Command arg : possibleArgs){
                if(Arrays.stream(arg.triggers).anyMatch(s -> s.equalsIgnoreCase(args[0]))){
                    arg.execute(Arrays.copyOfRange(args,1,args.length));
                }
            }
            reloadWorldRenderer();
        }else{
            mc.player.sendMessage(helpMessage(0));
        }
    }

    private void reloadWorldRenderer(){
        if(xray.isEnabled().isOn()){
            mc.worldRenderer.reload();
        }
    }
}
