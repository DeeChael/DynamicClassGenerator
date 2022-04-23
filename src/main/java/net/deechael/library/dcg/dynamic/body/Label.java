package net.deechael.library.dcg.dynamic.body;

import org.jetbrains.annotations.NotNull;

public final class Label implements Operation {

    private final String name;

    public Label(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String getString() {
        return this.name + ":";
    }

}
