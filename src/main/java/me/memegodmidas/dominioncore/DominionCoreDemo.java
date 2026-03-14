package me.memegodmidas.dominioncore;

import me.memegodmidas.dominioncore.command.CommandContext;
import me.memegodmidas.dominioncore.platform.RuntimeSide;

public class DominionCoreDemo {
    public static void main(String[] args) {
        DominionCoreBootstrap bootstrap = new DominionCoreBootstrap(RuntimeSide.CLIENT);
        bootstrap.initializeRuntime();

        String command = args.length == 0 ? "/DominionCore" : String.join(" ", args);
        bootstrap.commandRegistry().execute(command, new CommandContext(RuntimeSide.CLIENT));

        System.out.println("Executed: " + command);
        System.out.println("Active GUI: " + bootstrap.guiManager().activeScreen().orElseThrow());
        System.out.println("Open history: " + bootstrap.guiManager().openHistory());
        System.out.println("Registered keybinds: " + bootstrap.keybindRegistry().size());
        System.out.println("Registered commands: " + bootstrap.commandRegistry().size());
    }
}
