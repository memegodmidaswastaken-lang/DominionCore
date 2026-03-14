package me.memegodmidas.dominioncore.packaging;

import java.nio.file.Files;
import java.nio.file.Path;

public class ModMetadataSelfTest {
    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath();
        Path modsToml = root.resolve("src/main/resources/META-INF/mods.toml");
        Path fabricModJson = root.resolve("src/main/resources/fabric.mod.json");
        Path packMcmeta = root.resolve("src/main/resources/pack.mcmeta");

        assertFileContains(modsToml, "modId=\"dominioncore\"");
        assertFileContains(modsToml, "displayName=\"DominionCore\"");
        assertFileContains(fabricModJson, "\"id\": \"dominioncore\"");
        assertFileContains(fabricModJson, "\"name\": \"DominionCore\"");
        assertFileContains(packMcmeta, "\"pack_format\"");

        System.out.println("ModMetadataSelfTest passed");
    }

    private static void assertFileContains(Path path, String expectedText) throws Exception {
        if (!Files.exists(path)) {
            throw new IllegalStateException("Missing required metadata file: " + path);
        }
        String content = Files.readString(path);
        if (!content.contains(expectedText)) {
            throw new IllegalStateException("Metadata file does not contain expected text '" + expectedText + "': " + path);
        }
    }
}
