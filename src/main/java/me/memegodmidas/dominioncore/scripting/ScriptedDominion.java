package me.memegodmidas.dominioncore.scripting;

import java.util.List;
import java.util.Map;

public record ScriptedDominion(
        String name,
        String resource,
        Map<String, List<String>> eventActions,
        Map<String, List<String>> passiveRules,
        Map<String, List<String>> activeAbilities
) {
}
