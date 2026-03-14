package me.memegodmidas.dominioncore.input;

public record KeybindDefinition(
        String id,
        String displayName,
        String defaultKey,
        String category,
        Runnable onPressed
) {
    public void trigger() {
        onPressed.run();
    }
}
