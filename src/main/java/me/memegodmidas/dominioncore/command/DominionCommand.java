package me.memegodmidas.dominioncore.command;

import java.util.List;

public interface DominionCommand {
    String name();

    void execute(CommandContext context, List<String> args);
}
