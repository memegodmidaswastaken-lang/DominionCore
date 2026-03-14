package me.memegodmidas.dominioncore.gui;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class GuiManager {
    private final Map<ScreenId, GuiScreen> screens = new LinkedHashMap<>();

    public void register(GuiScreen screen) {
        if (screens.containsKey(screen.id())) {
            throw new IllegalArgumentException("Duplicate GUI screen id: " + screen.id());
        }
        screens.put(screen.id(), screen);
    }

    public Optional<GuiScreen> screen(ScreenId id) {
        return Optional.ofNullable(screens.get(id));
    }

    public void open(ScreenId id) {
        GuiScreen screen = screens.get(id);
        if (screen == null) {
            throw new IllegalArgumentException("GUI screen is not registered: " + id);
        }
        screen.open();
    }

    public Collection<GuiScreen> allScreens() {
        return Collections.unmodifiableCollection(screens.values());
    }

    public int size() {
        return screens.size();
    }
}
