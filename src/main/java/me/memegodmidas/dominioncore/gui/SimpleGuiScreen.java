package me.memegodmidas.dominioncore.gui;

import java.util.Objects;
import java.util.function.Consumer;

public record SimpleGuiScreen(ScreenId id, String title, Consumer<GuiScreen> onOpen) implements GuiScreen {
    public SimpleGuiScreen {
        Objects.requireNonNull(onOpen, "onOpen");
    }

    @Override
    public void open() {
        onOpen.accept(this);
    }
}
