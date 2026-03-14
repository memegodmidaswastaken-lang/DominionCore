package me.memegodmidas.dominioncore.command;

public interface DominionCommand {
    String name();

    void execute(CommandContext context);
}
