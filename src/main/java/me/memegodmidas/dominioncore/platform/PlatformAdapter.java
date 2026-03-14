package me.memegodmidas.dominioncore.platform;

public interface PlatformAdapter {
    RuntimeSide side();

    default boolean isClient() {
        return side() == RuntimeSide.CLIENT;
    }

    default boolean isDedicatedServer() {
        return side() == RuntimeSide.DEDICATED_SERVER;
    }
}
