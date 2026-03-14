# DominionCore (Bootstrap)

This repository now includes an initial implementation scaffold for the DominionCore framework described in `DESIGN_SPECIFICATION.md`.

## Included in this bootstrap
- Data models for bloodlines, dominions, and abilities.
- Generic registries with duplicate-id protection.
- JSON definition loader for config-driven content.
- Minimal DominionScript parser for `.dominion` files.
- Bootstrap wiring class (`DominionCoreBootstrap`) that loads JSON definitions and script-derived dominions.
- Example default config JSON files.
- Self-test entrypoints that can be executed without external test dependencies.

## Run self-tests
```bash
BUILD_DIR=$(mktemp -d)
javac -d "$BUILD_DIR/main" $(find src/main/java -name '*.java')
javac -cp "$BUILD_DIR/main" -d "$BUILD_DIR/test" $(find src/test/java -name '*.java')
java -cp "$BUILD_DIR/main:$BUILD_DIR/test" me.memegodmidas.dominioncore.loader.JsonDefinitionLoaderSelfTest
java -cp "$BUILD_DIR/main:$BUILD_DIR/test" me.memegodmidas.dominioncore.scripting.SimpleDominionScriptEngineSelfTest
```
