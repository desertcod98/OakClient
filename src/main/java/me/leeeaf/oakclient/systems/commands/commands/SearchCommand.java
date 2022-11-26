package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.systems.commands.Command;
import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


import java.util.ArrayList;
import java.util.Arrays;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class SearchCommand extends Command {
    public static ArrayList<Block> searchTargets = new ArrayList<>();

    public SearchCommand() {
        super("search", "Manages search targets (blocks)", new String[]{"search"}, new Command[]{
                new Command("list", "show enabled targets", new String[]{"list"}, null) {
                    @Override
                    public void excecute(String[] args) {
                        if(searchTargets.size()>0){
                            mc.player.sendMessage(Text.literal("Targets:").formatted(Formatting.GREEN));
                            for(Block block: searchTargets){
                                mc.player.sendMessage(Text.of(Registry.BLOCK.getId(block).toString()));
                            }
                        }else{
                            mc.player.sendMessage(Text.of("No targets in list! Add them with .search add"));
                        }
                    }
                },
                new Command("add", "adds target", new String[]{"add"}, null) {
                    @Override
                    public void excecute(String[] args) {
                        if(args.length>0){
                            Identifier blockToAddId = new Identifier(args[0]);
                            Block blockToAdd = Registry.BLOCK.get(blockToAddId);
                            if(!blockToAdd.equals(Registry.BLOCK.get(Registry.BLOCK.getDefaultId()))){
                                if(!searchTargets.contains(blockToAdd)){
                                    searchTargets.add(blockToAdd);
                                    mc.player.sendMessage(Text.literal("Added to search targets: ").append(Text.literal(args[0]).formatted(Formatting.AQUA)));
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
                    public void excecute(String[] args) {
                        if(args.length>0){
                            Identifier blockToRemoveId = new Identifier(args[0]);
                            Block blockToRemove = Registry.BLOCK.get(blockToRemoveId);
                            if(searchTargets.remove(blockToRemove)){
                                mc.player.sendMessage(Text.literal("Removed from search targets: ").append(Text.literal(args[0]).formatted(Formatting.AQUA)));
                            }else{
                                mc.player.sendMessage(Text.literal("Block not found!"));
                            }
                        }
                    }
                },
                new Command("clear", "clears target list", new String[]{"clear"}, null) {
                    @Override
                    public void excecute(String[] args) {
                        searchTargets.clear(); //todo test
                        mc.player.sendMessage(Text.of("Search target list cleared!"));
                    }
                },
        });
    }

    @Override
    public void excecute(String[] args) {
        if(args.length>0){
            for(Command arg : possibleArgs){
                if(Arrays.stream(arg.triggers).anyMatch(s -> s.equalsIgnoreCase(args[0]))){
                    arg.excecute(Arrays.copyOfRange(args,1,args.length));
                }
            }
        }else{
            mc.player.sendMessage(helpMessage(0));
        }
    }

}
