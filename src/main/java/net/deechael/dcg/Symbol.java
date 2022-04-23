package net.deechael.dcg;

public enum Symbol {

    EQUAL("=="),
    NOT("!"),
    NOT_EQUAL("!="),
    GREATER(">"),
    SMALLER("<"),
    GREATER_OR_EQUAL(">="),
    SMALLER_OR_EQUAL("<="),
    AND("&&"),
    OR("||");

    private final String symbolString;

    Symbol(String symbolString) {
        this.symbolString = symbolString;
    }

    public String getString() {
        return symbolString;
    }

}
