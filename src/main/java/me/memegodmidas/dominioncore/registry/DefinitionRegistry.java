package me.memegodmidas.dominioncore.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class DefinitionRegistry<T> {
    private final Map<String, T> entries = new LinkedHashMap<>();

    public void register(String id, T value) {
        if (entries.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate registry id: " + id);
        }
        entries.put(id, value);
    }

    public Optional<T> get(String id) {
        return Optional.ofNullable(entries.get(id));
    }

    public Collection<T> values() {
        return Collections.unmodifiableCollection(entries.values());
    }

    public int size() {
        return entries.size();
    }

    public void clear() {
        entries.clear();
    }
}
