package me.memegodmidas.dominioncore.input;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class KeybindRegistry {
    private final Map<String, KeybindDefinition> keybinds = new LinkedHashMap<>();

    public void register(KeybindDefinition keybind) {
        if (keybinds.containsKey(keybind.id())) {
            throw new IllegalArgumentException("Duplicate keybind id: " + keybind.id());
        }
        keybinds.put(keybind.id(), keybind);
    }

    public Collection<KeybindDefinition> all() {
        return Collections.unmodifiableCollection(keybinds.values());
    }

    public void trigger(String keybindId) {
        KeybindDefinition definition = keybinds.get(keybindId);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown keybind id: " + keybindId);
        }
        definition.trigger();
    }

    public int size() {
        return keybinds.size();
    }
}
