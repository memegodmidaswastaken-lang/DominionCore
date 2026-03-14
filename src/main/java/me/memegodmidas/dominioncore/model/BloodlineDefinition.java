package me.memegodmidas.dominioncore.model;

import java.util.List;

public record BloodlineDefinition(
        String id,
        String name,
        String resource,
        String scalingCondition,
        int maxPrestige,
        List<AbilityDefinition> abilities,
        List<String> mutationSlots
) {
}
