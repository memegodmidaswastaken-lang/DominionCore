# DominionCore (Bootstrap)

This repository includes an implementation scaffold for the DominionCore framework described in `DESIGN_SPECIFICATION.md`.

## Included in this bootstrap
- Data models for bloodlines, dominions, and abilities.
- Generic registries with duplicate-id protection.
- JSON definition loader for config-driven content.
- Minimal DominionScript parser for `.dominion` files.
- Runtime compatibility split for dedicated server vs client-side systems.
- Client GUI manager with registered DominionCore screens.
- Keybind registry for opening GUI screens.
- Command registry with `/DominionCore` command and GUI target arguments.
- Bootstrap wiring class (`DominionCoreBootstrap`) that loads JSON definitions and script-derived dominions.
- Example default config JSON files.
- Forge/Fabric mod metadata files so mod loaders can identify the project as a mod (`META-INF/mods.toml`, `fabric.mod.json`, `pack.mcmeta`).
- Self-test entrypoints that can be executed without external test dependencies.

## Runtime behavior
- `RuntimeSide.DEDICATED_SERVER`: loads gameplay/data systems only, no GUI/keybind registration.
- `RuntimeSide.CLIENT`: registers DominionCore GUI screens and keybinds (`V`, `G`, `H`, `J`) for opening menus.
- Core command `/DominionCore` is registered and opens GUI targets on client contexts:
  - `/DominionCore` (default dominion manager)
  - `/DominionCore bloodline`
  - `/DominionCore dominion`
  - `/DominionCore faction`
  - `/DominionCore religion`
  - `/DominionCore hud`

## Mod loader metadata
- Forge/NeoForge descriptor: `src/main/resources/META-INF/mods.toml`
- Fabric descriptor: `src/main/resources/fabric.mod.json`
- Resource pack metadata: `src/main/resources/pack.mcmeta`

## Quick demo run
```bash
BUILD_DIR=$(mktemp -d)
javac -d "$BUILD_DIR/main" $(find src/main/java -name '*.java')
java -cp "$BUILD_DIR/main" me.memegodmidas.dominioncore.DominionCoreDemo /DominionCore faction
```

## Run self-tests
```bash
BUILD_DIR=$(mktemp -d)
javac -d "$BUILD_DIR/main" $(find src/main/java -name '*.java')
javac -cp "$BUILD_DIR/main" -d "$BUILD_DIR/test" $(find src/test/java -name '*.java')
java -cp "$BUILD_DIR/main:$BUILD_DIR/test" me.memegodmidas.dominioncore.loader.JsonDefinitionLoaderSelfTest
java -cp "$BUILD_DIR/main:$BUILD_DIR/test" me.memegodmidas.dominioncore.scripting.SimpleDominionScriptEngineSelfTest
java -cp "$BUILD_DIR/main:$BUILD_DIR/test" me.memegodmidas.dominioncore.lifecycle.DominionRuntimeSelfTest
java -cp "$BUILD_DIR/main:$BUILD_DIR/test" me.memegodmidas.dominioncore.packaging.ModMetadataSelfTest
```
