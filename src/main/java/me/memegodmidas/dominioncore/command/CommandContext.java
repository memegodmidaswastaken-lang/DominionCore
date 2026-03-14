package me.memegodmidas.dominioncore.command;

import me.memegodmidas.dominioncore.platform.RuntimeSide;

public record CommandContext(RuntimeSide runtimeSide) {
    public boolean isClient() {
        return runtimeSide == RuntimeSide.CLIENT;
    }
}
