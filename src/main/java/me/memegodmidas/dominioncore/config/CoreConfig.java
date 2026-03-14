package me.memegodmidas.dominioncore.config;

public record CoreConfig(
        boolean bloodlinesEnabled,
        boolean dominionsEnabled,
        boolean factionsEnabled,
        boolean religionEnabled,
        boolean scriptingEnabled
) {
    public static CoreConfig defaults() {
        return new CoreConfig(true, true, true, true, true);
    }
}
