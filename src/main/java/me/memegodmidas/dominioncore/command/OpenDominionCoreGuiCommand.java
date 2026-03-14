package me.memegodmidas.dominioncore.command;

import me.memegodmidas.dominioncore.gui.GuiManager;
import me.memegodmidas.dominioncore.gui.ScreenId;

import java.util.List;

public class OpenDominionCoreGuiCommand implements DominionCommand {
    private final GuiManager guiManager;

    public OpenDominionCoreGuiCommand(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public String name() {
        return "/DominionCore";
    }

    @Override
    public void execute(CommandContext context, List<String> args) {
        if (!context.isClient()) {
            throw new IllegalStateException("/DominionCore can only open GUI on client side");
        }
        ScreenId target = mapScreen(args);
        guiManager.open(target);
    }

    private ScreenId mapScreen(List<String> args) {
        if (args.isEmpty()) {
            return ScreenId.DOMINION_MANAGER;
        }
        return switch (args.getFirst().toLowerCase()) {
            case "bloodline" -> ScreenId.BLOODLINE_SELECTION;
            case "dominion" -> ScreenId.DOMINION_MANAGER;
            case "faction" -> ScreenId.FACTION_MENU;
            case "religion" -> ScreenId.RELIGION_MENU;
            case "hud" -> ScreenId.HUD_EDITOR;
            default -> throw new IllegalArgumentException("Unknown DominionCore GUI target: " + args.getFirst());
        };
    }
}
