package me.memegodmidas.dominioncore.command;

import me.memegodmidas.dominioncore.gui.GuiManager;
import me.memegodmidas.dominioncore.gui.ScreenId;
import me.memegodmidas.dominioncore.platform.RuntimeSide;

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
    public void execute(CommandContext context) {
        if (context.runtimeSide() != RuntimeSide.CLIENT) {
            throw new IllegalStateException("/DominionCore can only open GUI on client side");
        }
        guiManager.open(ScreenId.DOMINION_MANAGER);
    }
}
