package me.memegodmidas.dominioncore.loader;

import me.memegodmidas.dominioncore.model.BloodlineDefinition;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonDefinitionLoaderSelfTest {
    public static void main(String[] args) throws Exception {
        Path tempDir = Files.createTempDirectory("dominioncore-loader-test");
        Path bloodlines = tempDir.resolve("bloodlines");
        Files.createDirectories(bloodlines);
        Files.writeString(bloodlines.resolve("test.json"), """
                {
                  "id": "test",
                  "name": "Test",
                  "resource": "essence",
                  "scalingCondition": "kills",
                  "maxPrestige": 5,
                  "abilities": [],
                  "mutationSlots": []
                }
                """);

        JsonDefinitionLoader loader = new JsonDefinitionLoader();
        List<BloodlineDefinition> defs = loader.loadDirectory(bloodlines, BloodlineDefinition.class);

        if (defs.size() != 1 || !"test".equals(defs.getFirst().id()) || !"essence".equals(defs.getFirst().resource())) {
            throw new IllegalStateException("JsonDefinitionLoaderSelfTest failed: unexpected parsed values");
        }

        System.out.println("JsonDefinitionLoaderSelfTest passed");
    }
}
