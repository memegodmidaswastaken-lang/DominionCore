package me.memegodmidas.dominioncore.scripting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Minimal parser used as a bootstrapping implementation for DominionScript.
 * It captures core blocks and lines so they can be validated and inspected.
 */
public class SimpleDominionScriptEngine implements DominionScriptEngine {
    private static final Pattern DOMINION_HEADER = Pattern.compile("^dominion\\s+\"(.+)\":$");

    @Override
    public List<ScriptedDominion> loadScripts(Path scriptsDirectory) {
        if (!Files.exists(scriptsDirectory)) {
            return List.of();
        }

        try (Stream<Path> files = Files.list(scriptsDirectory)) {
            return files
                    .filter(path -> path.getFileName().toString().endsWith(".dominion"))
                    .map(this::parseFile)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load scripts from " + scriptsDirectory, e);
        }
    }

    private ScriptedDominion parseFile(Path scriptFile) {
        try {
            List<String> lines = Files.readAllLines(scriptFile);
            return parseLines(lines, scriptFile);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read script " + scriptFile, e);
        }
    }

    ScriptedDominion parseLines(List<String> rawLines, Path source) {
        String name = null;
        String resource = "";
        Map<String, List<String>> scaling = new LinkedHashMap<>();
        Map<String, List<String>> passive = new LinkedHashMap<>();
        Map<String, List<String>> activeAbilities = new LinkedHashMap<>();

        String section = "";
        String currentKey = "default";

        for (String line : rawLines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }

            Matcher headerMatcher = DOMINION_HEADER.matcher(trimmed);
            if (headerMatcher.matches()) {
                name = headerMatcher.group(1);
                continue;
            }

            if (trimmed.startsWith("resource:")) {
                resource = extractQuotedValue(trimmed);
                continue;
            }
            if (trimmed.equals("scaling:")) {
                section = "scaling";
                currentKey = "default";
                continue;
            }
            if (trimmed.equals("passive:")) {
                section = "passive";
                currentKey = "default";
                continue;
            }
            if (trimmed.startsWith("active ")) {
                section = "active";
                currentKey = trimmed.substring("active ".length()).replace(":", "").replace("\"", "").trim();
                activeAbilities.putIfAbsent(currentKey, new ArrayList<>());
                continue;
            }

            if (trimmed.startsWith("on ") && trimmed.endsWith(":")) {
                currentKey = trimmed.substring(0, trimmed.length() - 1);
                scaling.putIfAbsent(currentKey, new ArrayList<>());
                continue;
            }

            if (Objects.equals(section, "scaling")) {
                scaling.computeIfAbsent(currentKey, ignored -> new ArrayList<>()).add(trimmed);
            } else if (Objects.equals(section, "passive")) {
                passive.computeIfAbsent(currentKey, ignored -> new ArrayList<>()).add(trimmed);
            } else if (Objects.equals(section, "active")) {
                activeAbilities.computeIfAbsent(currentKey, ignored -> new ArrayList<>()).add(trimmed);
            }
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Missing dominion header in script: " + source);
        }

        return new ScriptedDominion(name, resource, scaling, passive, activeAbilities);
    }

    private String extractQuotedValue(String line) {
        int firstQuote = line.indexOf('"');
        int secondQuote = line.lastIndexOf('"');
        if (firstQuote < 0 || secondQuote <= firstQuote) {
            return line.substring(line.indexOf(':') + 1).trim();
        }
        return line.substring(firstQuote + 1, secondQuote);
    }
}
