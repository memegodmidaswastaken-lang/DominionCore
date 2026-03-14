package me.memegodmidas.dominioncore.lifecycle;

import me.memegodmidas.dominioncore.DominionCoreBootstrap;
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

        DominionCoreBootstrap clientBootstrap = new DominionCoreBootstrap(RuntimeSide.CLIENT);
        clientBootstrap.initializeRuntime();
        if (clientBootstrap.guiManager().size() < 4) {
            throw new IllegalStateException("Client should register GUI screens");
        }
        if (clientBootstrap.keybindRegistry().size() < 4) {
            throw new IllegalStateException("Client should register keybinds");
        }

        clientBootstrap.keybindRegistry().trigger("open_dominion_manager");

        System.out.println("DominionRuntimeSelfTest passed");
    }
}
