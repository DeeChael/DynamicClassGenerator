package net.deechael.dcg.items;

import net.deechael.dcg.JEnum;
import net.deechael.dcg.JType;

final class EnumVar implements Var {

    private final String enumClass;
    private final String enumItemName;

    public EnumVar(JEnum enumClass, String enumItemName) {
        this.enumClass = enumClass.getName();
        this.enumItemName = enumItemName;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("No type");
    }

    @Override
    public String getName() {
        throw new RuntimeException("No name");
    }

    @Override
    public String varString() {
        return enumClass + "." + enumItemName;
    }

}
