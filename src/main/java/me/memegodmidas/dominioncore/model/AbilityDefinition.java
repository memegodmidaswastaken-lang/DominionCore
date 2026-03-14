package me.memegodmidas.dominioncore.model;

import java.util.Map;

public record AbilityDefinition(
        String id,
        String name,
        String description,
        AbilityType type,
        int baseCost,
        Map<String, String> scalingFormula
) {
    public enum AbilityType {
        PASSIVE,
        ACTIVE
    }
}
