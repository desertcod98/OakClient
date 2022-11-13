//package me.leeeaf.oakclient.systems.commands.commands;
//
//import me.leeeaf.oakclient.OakClientClient;
//import me.leeeaf.oakclient.systems.modules.Module;
//import me.leeeaf.oakclient.systems.commands.Command;
//import me.leeeaf.oakclient.systems.modules.ModuleRegistry;
//import net.minecraft.text.Text;
//
//public class ToggleCommand extends Command {
//    public ToggleCommand() {
//            super("Toggle", "Toggles a module", new String[]{"toggle"});
//    }
//
//    @Override
//    public void excecute(String[] args) {
//        if(args.length == 0){
//            OakClientClient.mc.player.sendMessage(Text.of("Provide module name"));
//            return;
//        }
//        String moduleName = args[0];
//        for(Module module: ModuleRegistry.getModules()){
//            if(module.name.equalsIgnoreCase(moduleName)){
//                module.setEnabled(!module.isEnabled());
//                return;
//            }
//        }
//        OakClientClient.mc.player.sendMessage(Text.of("Module not found"));
//    }
//}
