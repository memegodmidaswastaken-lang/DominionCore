package me.memegodmidas.dominioncore.model;

import java.util.List;

public record DominionDefinition(
        String id,
        String name,
        String source,
        String formula,
        List<String> maintenanceConditions,
        List<String> modifiers
) {
}
