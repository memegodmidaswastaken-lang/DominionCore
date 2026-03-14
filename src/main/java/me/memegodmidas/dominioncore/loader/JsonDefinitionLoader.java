package me.memegodmidas.dominioncore.loader;

import me.memegodmidas.dominioncore.model.AbilityDefinition;
import me.memegodmidas.dominioncore.model.BloodlineDefinition;
import me.memegodmidas.dominioncore.model.DominionDefinition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JsonDefinitionLoader {

    public <T> List<T> loadDirectory(Path directory, Class<T> type) {
        if (!Files.exists(directory)) {
            return List.of();
        }

        try (Stream<Path> files = Files.list(directory)) {
            List<Path> jsonFiles = files
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();

            List<T> results = new ArrayList<>();
            for (Path jsonFile : jsonFiles) {
                results.add(loadFile(jsonFile, type));
            }
            return results;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read definition directory: " + directory, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T loadFile(Path file, Class<T> type) {
        try {
            String json = Files.readString(file);
            Object parsed = MiniJsonParser.parse(json);
            if (!(parsed instanceof Map<?, ?> object)) {
                throw new IllegalArgumentException("Root json value must be an object");
            }

            if (type.equals(BloodlineDefinition.class)) {
                return (T) toBloodline((Map<String, Object>) object);
            }
            if (type.equals(DominionDefinition.class)) {
                return (T) toDominion((Map<String, Object>) object);
            }
            throw new IllegalArgumentException("Unsupported definition type: " + type.getName());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse json file: " + file, e);
        }
    }

    @SuppressWarnings("unchecked")
    private BloodlineDefinition toBloodline(Map<String, Object> object) {
        List<AbilityDefinition> abilities = new ArrayList<>();
        for (Object entry : list(object, "abilities")) {
            Map<String, Object> abilityMap = (Map<String, Object>) entry;
            Map<String, String> scaling = new LinkedHashMap<>();
            for (Map.Entry<String, Object> scaleEntry : map(abilityMap, "scalingFormula").entrySet()) {
                scaling.put(scaleEntry.getKey(), String.valueOf(scaleEntry.getValue()));
            }

            abilities.add(new AbilityDefinition(
                    string(abilityMap, "id"),
                    string(abilityMap, "name"),
                    string(abilityMap, "description"),
                    AbilityDefinition.AbilityType.valueOf(string(abilityMap, "type")),
                    number(abilityMap, "baseCost").intValue(),
                    scaling
            ));
        }

        List<String> mutationSlots = list(object, "mutationSlots").stream().map(String::valueOf).toList();

        return new BloodlineDefinition(
                string(object, "id"),
                string(object, "name"),
                string(object, "resource"),
                string(object, "scalingCondition"),
                number(object, "maxPrestige").intValue(),
                abilities,
                mutationSlots
        );
    }

    private DominionDefinition toDominion(Map<String, Object> object) {
        List<String> maintenance = list(object, "maintenanceConditions").stream().map(String::valueOf).toList();
        List<String> modifiers = list(object, "modifiers").stream().map(String::valueOf).toList();

        return new DominionDefinition(
                string(object, "id"),
                string(object, "name"),
                string(object, "source"),
                string(object, "formula"),
                maintenance,
                modifiers
        );
    }

    @SuppressWarnings("unchecked")
    private List<Object> list(Map<String, Object> object, String key) {
        return (List<Object>) object.getOrDefault(key, List.of());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> map(Map<String, Object> object, String key) {
        return (Map<String, Object>) object.getOrDefault(key, Map.of());
    }

    private String string(Map<String, Object> object, String key) {
        Object value = object.get(key);
        return value == null ? "" : String.valueOf(value);
    }

    private Number number(Map<String, Object> object, String key) {
        Object value = object.get(key);
        if (value instanceof Number number) {
            return number;
        }
        throw new IllegalArgumentException("Expected numeric value for key: " + key);
    }
}
