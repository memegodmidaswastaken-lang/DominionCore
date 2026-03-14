package me.memegodmidas.dominioncore.scripting;

import java.nio.file.Path;
import java.util.List;

public class SimpleDominionScriptEngineSelfTest {
    public static void main(String[] args) {
        SimpleDominionScriptEngine engine = new SimpleDominionScriptEngine();

        ScriptedDominion dominion = engine.parseLines(List.of(
                "dominion \"ShadowWalker\":",
                "resource: \"shadow_essence\"",
                "scaling:",
                "    on kill:",
                "        add 10 shadow_essence",
                "passive:",
                "    if shadow_essence > 50:",
                "        apply invisibility 1 to self",
                "active \"Void Step\":",
                "    cost: 30 shadow_essence",
                "    on use:",
                "        teleport self to cursor_target"
        ), Path.of("shadowwalker.dominion"));

        if (!"ShadowWalker".equals(dominion.name())) {
            throw new IllegalStateException("Expected name ShadowWalker");
        }
        if (!"shadow_essence".equals(dominion.resource())) {
            throw new IllegalStateException("Expected resource shadow_essence");
        }
        if (!dominion.eventActions().containsKey("on kill")) {
            throw new IllegalStateException("Missing scaling on kill block");
        }
        if (!dominion.activeAbilities().containsKey("Void Step")) {
            throw new IllegalStateException("Missing active ability block");
        }

        System.out.println("SimpleDominionScriptEngineSelfTest passed");
    }
}
