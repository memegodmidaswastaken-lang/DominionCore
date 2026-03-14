package me.memegodmidas.dominioncore;

import me.memegodmidas.dominioncore.command.CommandRegistry;
import me.memegodmidas.dominioncore.config.CoreConfig;
import me.memegodmidas.dominioncore.gui.GuiManager;
import me.memegodmidas.dominioncore.input.KeybindRegistry;
import me.memegodmidas.dominioncore.lifecycle.DominionRuntime;
import me.memegodmidas.dominioncore.loader.JsonDefinitionLoader;
import me.memegodmidas.dominioncore.model.BloodlineDefinition;
import me.memegodmidas.dominioncore.model.DominionDefinition;
import me.memegodmidas.dominioncore.platform.PlatformAdapter;
import me.memegodmidas.dominioncore.platform.RuntimeSide;
import me.memegodmidas.dominioncore.platform.SimplePlatformAdapter;
import me.memegodmidas.dominioncore.registry.BloodlineRegistry;
import me.memegodmidas.dominioncore.registry.DominionRegistry;
import me.memegodmidas.dominioncore.scripting.DominionScriptEngine;
import me.memegodmidas.dominioncore.scripting.ScriptedDominion;
import me.memegodmidas.dominioncore.scripting.SimpleDominionScriptEngine;

import java.nio.file.Path;
import java.util.List;

/**
 * Foundation bootstrap for wiring data-driven systems.
 */
public class DominionCoreBootstrap {
    private final CoreConfig config;
    private final JsonDefinitionLoader definitionLoader;
    private final DominionScriptEngine scriptEngine;
    private final BloodlineRegistry bloodlineRegistry;
    private final DominionRegistry dominionRegistry;
    private final PlatformAdapter platform;
    private final GuiManager guiManager;
    private final KeybindRegistry keybindRegistry;
    private final CommandRegistry commandRegistry;
    private final DominionRuntime runtime;

    public DominionCoreBootstrap() {
        this(
                CoreConfig.defaults(),
                new JsonDefinitionLoader(),
                new SimpleDominionScriptEngine(),
                new BloodlineRegistry(),
                new DominionRegistry(),
                new SimplePlatformAdapter(RuntimeSide.DEDICATED_SERVER),
                new GuiManager(),
                new KeybindRegistry(),
                new CommandRegistry()
        );
    }

    public DominionCoreBootstrap(RuntimeSide runtimeSide) {
        this(
                CoreConfig.defaults(),
                new JsonDefinitionLoader(),
                new SimpleDominionScriptEngine(),
                new BloodlineRegistry(),
                new DominionRegistry(),
                new SimplePlatformAdapter(runtimeSide),
                new GuiManager(),
                new KeybindRegistry(),
                new CommandRegistry()
        );
    }

    DominionCoreBootstrap(
            CoreConfig config,
            JsonDefinitionLoader definitionLoader,
            DominionScriptEngine scriptEngine,
            BloodlineRegistry bloodlineRegistry,
            DominionRegistry dominionRegistry,
            PlatformAdapter platform,
            GuiManager guiManager,
            KeybindRegistry keybindRegistry,
            CommandRegistry commandRegistry
    ) {
        this.config = config;
        this.definitionLoader = definitionLoader;
        this.scriptEngine = scriptEngine;
        this.bloodlineRegistry = bloodlineRegistry;
        this.dominionRegistry = dominionRegistry;
        this.platform = platform;
        this.guiManager = guiManager;
        this.keybindRegistry = keybindRegistry;
        this.commandRegistry = commandRegistry;
        this.runtime = new DominionRuntime(platform, guiManager, keybindRegistry, commandRegistry);
    }

    public void initializeRuntime() {
        runtime.initialize();
    }

    public void loadFromPath(Path configRoot, Path scriptsRoot) {
        if (config.bloodlinesEnabled()) {
            List<BloodlineDefinition> bloodlines = definitionLoader.loadDirectory(configRoot.resolve("bloodlines"), BloodlineDefinition.class);
            bloodlineRegistry.clear();
            bloodlines.forEach(def -> bloodlineRegistry.register(def.id(), def));
        }

        if (config.dominionsEnabled()) {
            List<DominionDefinition> dominions = definitionLoader.loadDirectory(configRoot.resolve("dominions"), DominionDefinition.class);
            dominionRegistry.clear();
            dominions.forEach(def -> dominionRegistry.register(def.id(), def));
        }

        if (config.scriptingEnabled()) {
            List<ScriptedDominion> scriptedDominions = scriptEngine.loadScripts(scriptsRoot);
            scriptedDominions.forEach(scripted -> {
                String id = scripted.name().toLowerCase().replace(' ', '_');
                if (dominionRegistry.get(id).isEmpty()) {
                    dominionRegistry.register(
                            id,
                            new DominionDefinition(
                                    id,
                                    scripted.name(),
                                    "script",
                                    "dynamic",
                                    List.of(),
                                    List.of("resource=" + scripted.resource())
                            )
                    );
                }
            });
        }
    }

    public BloodlineRegistry bloodlineRegistry() {
        return bloodlineRegistry;
    }

    public DominionRegistry dominionRegistry() {
        return dominionRegistry;
    }

    public GuiManager guiManager() {
        return guiManager;
    }

    public KeybindRegistry keybindRegistry() {
        return keybindRegistry;
    }

    public PlatformAdapter platform() {
        return platform;
    }

    public CommandRegistry commandRegistry() {
        return commandRegistry;
    }
}
