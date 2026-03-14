package me.memegodmidas.dominioncore.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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
        ParsedCommand parsedCommand = parse(rawCommand);
        DominionCommand command = commands.get(parsedCommand.command());
        if (command == null) {
            throw new IllegalArgumentException("Unknown command: " + rawCommand);
        }
        command.execute(context, parsedCommand.args());
    }

    public Collection<DominionCommand> all() {
        return Collections.unmodifiableCollection(commands.values());
    }

    public int size() {
        return commands.size();
    }

    private ParsedCommand parse(String rawCommand) {
        String[] parts = rawCommand.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isBlank()) {
            throw new IllegalArgumentException("Command cannot be blank");
        }
        String commandName = normalize(parts[0]);
        List<String> args = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isBlank()) {
                args.add(parts[i]);
            }
        }
        return new ParsedCommand(commandName, List.copyOf(args));
    }

    private String normalize(String value) {
        return value.trim().toLowerCase();
    }

    private record ParsedCommand(String command, List<String> args) {
    }
}
