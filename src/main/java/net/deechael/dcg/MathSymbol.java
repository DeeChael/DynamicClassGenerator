package net.deechael.dcg;

public enum MathSymbol {

    PLUS("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String symbolString;

    MathSymbol(String symbolString) {
        this.symbolString = symbolString;
    }

    public String getString() {
        return symbolString;
    }

}
