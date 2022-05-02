package net.deechael.dcg;

public enum DigitOperator {

    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    REMINDER("%"),
    BITWISE_NOT("~"),
    BITWISE_AND("&"),
    BITWISE_OR("|"),
    BITWISE_XOR("^"),
    BITWISE_LEFT_SHIFT("<<"),
    BITWISE_RIGHT_SHIFT(">>"),
    BITWISE_RIGHT_LOGICAL_SHIFT(">"),
    INCREASE("++"),
    DECREASE("--");

    private final String symbolString;

    DigitOperator(String symbolString) {
        this.symbolString = symbolString;
    }

    public String getSymbol() {
        return symbolString;
    }

    public String getString() {
        return symbolString;
    }

}
