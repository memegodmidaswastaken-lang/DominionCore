package me.memegodmidas.dominioncore.scripting;

import java.nio.file.Path;
import java.util.List;

public interface DominionScriptEngine {
    List<ScriptedDominion> loadScripts(Path scriptsDirectory);
}
