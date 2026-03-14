package me.memegodmidas.dominioncore.lifecycle;

import me.memegodmidas.dominioncore.DominionCoreBootstrap;
import me.memegodmidas.dominioncore.command.CommandContext;
import me.memegodmidas.dominioncore.platform.RuntimeSide;

public class DominionRuntimeSelfTest {
    public static void main(String[] args) {
        DominionCoreBootstrap serverBootstrap = new DominionCoreBootstrap(RuntimeSide.DEDICATED_SERVER);
        serverBootstrap.initializeRuntime();
        if (serverBootstrap.guiManager().size() != 0) {
            throw new IllegalStateException("Server should not register GUI screens");
        }
        if (serverBootstrap.keybindRegistry().size() != 0) {
            throw new IllegalStateException("Server should not register keybinds");
        }
        if (serverBootstrap.commandRegistry().size() != 1) {
            throw new IllegalStateException("Server should register core commands");
        }

        boolean blockedOnServer = false;
        try {
            serverBootstrap.commandRegistry().execute("/DominionCore", new CommandContext(RuntimeSide.DEDICATED_SERVER));
        } catch (IllegalStateException expected) {
            blockedOnServer = true;
        }
        if (!blockedOnServer) {
            throw new IllegalStateException("/DominionCore command must reject server-only GUI opening");
        }

        DominionCoreBootstrap clientBootstrap = new DominionCoreBootstrap(RuntimeSide.CLIENT);
        clientBootstrap.initializeRuntime();
        if (clientBootstrap.guiManager().size() < 4) {
            throw new IllegalStateException("Client should register GUI screens");
        }
        if (clientBootstrap.keybindRegistry().size() < 4) {
            throw new IllegalStateException("Client should register keybinds");
        }

        clientBootstrap.keybindRegistry().trigger("open_dominion_manager");
        clientBootstrap.commandRegistry().execute("/DominionCore", new CommandContext(RuntimeSide.CLIENT));

        System.out.println("DominionRuntimeSelfTest passed");
    }
}
