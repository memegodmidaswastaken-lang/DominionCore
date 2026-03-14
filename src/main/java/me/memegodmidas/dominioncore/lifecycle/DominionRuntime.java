package me.memegodmidas.dominioncore.lifecycle;

import me.memegodmidas.dominioncore.command.CommandRegistry;
import me.memegodmidas.dominioncore.command.OpenDominionCoreGuiCommand;
import me.memegodmidas.dominioncore.gui.GuiManager;
import me.memegodmidas.dominioncore.gui.ScreenId;
import me.memegodmidas.dominioncore.gui.SimpleGuiScreen;
import me.memegodmidas.dominioncore.input.KeybindDefinition;
import me.memegodmidas.dominioncore.input.KeybindRegistry;
import me.memegodmidas.dominioncore.platform.PlatformAdapter;

public class DominionRuntime {
    private final PlatformAdapter platform;
    private final GuiManager guiManager;
    private final KeybindRegistry keybindRegistry;
    private final CommandRegistry commandRegistry;

    public DominionRuntime(PlatformAdapter platform, GuiManager guiManager, KeybindRegistry keybindRegistry, CommandRegistry commandRegistry) {
        this.platform = platform;
        this.guiManager = guiManager;
        this.keybindRegistry = keybindRegistry;
        this.commandRegistry = commandRegistry;
    }

    public void initialize() {
        if (platform.isClient()) {
            registerClientScreens();
            registerClientKeybinds();
        }
        registerCommands();
    }

    private void registerClientScreens() {
        guiManager.register(new SimpleGuiScreen(ScreenId.BLOODLINE_SELECTION, "Bloodline Selection", ignored -> { }));
        guiManager.register(new SimpleGuiScreen(ScreenId.DOMINION_MANAGER, "Dominion Manager", ignored -> { }));
        guiManager.register(new SimpleGuiScreen(ScreenId.FACTION_MENU, "Faction Menu", ignored -> { }));
        guiManager.register(new SimpleGuiScreen(ScreenId.RELIGION_MENU, "Religion Menu", ignored -> { }));
        guiManager.register(new SimpleGuiScreen(ScreenId.HUD_EDITOR, "HUD Editor", ignored -> { }));
    }

    private void registerCommands() {
        commandRegistry.register(new OpenDominionCoreGuiCommand(guiManager));
    }

    private void registerClientKeybinds() {
        keybindRegistry.register(new KeybindDefinition(
                "open_dominion_manager",
                "Open Dominion Manager",
                "V",
                "DominionCore",
                () -> guiManager.open(ScreenId.DOMINION_MANAGER)
        ));
        keybindRegistry.register(new KeybindDefinition(
                "open_faction_menu",
                "Open Faction Menu",
                "G",
                "DominionCore",
                () -> guiManager.open(ScreenId.FACTION_MENU)
        ));
        keybindRegistry.register(new KeybindDefinition(
                "open_religion_menu",
                "Open Religion Menu",
                "H",
                "DominionCore",
                () -> guiManager.open(ScreenId.RELIGION_MENU)
        ));
        keybindRegistry.register(new KeybindDefinition(
                "open_hud_editor",
                "Open HUD Editor",
                "J",
                "DominionCore",
                () -> guiManager.open(ScreenId.HUD_EDITOR)
        ));
    }
}
