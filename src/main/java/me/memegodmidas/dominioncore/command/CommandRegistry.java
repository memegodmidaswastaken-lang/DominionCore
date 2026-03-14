package me.memegodmidas.dominioncore.command;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, DominionCommand> commands = new LinkedHashMap<>();

    public void register(DominionCommand command) {
        String normalized = normalize(command.name());
        if (commands.containsKey(normalized)) {
            throw new IllegalArgumentException("Duplicate command: " + command.name());
        }
        commands.put(normalized, command);
    }

    public void execute(String rawCommand, CommandContext context) {
        DominionCommand command = commands.get(normalize(rawCommand));
        if (command == null) {
            throw new IllegalArgumentException("Unknown command: " + rawCommand);
        }
        command.execute(context);
    }

    public Collection<DominionCommand> all() {
        return Collections.unmodifiableCollection(commands.values());
    }

    public int size() {
        return commands.size();
    }

    private String normalize(String value) {
        return value.trim().toLowerCase();
    }
}
