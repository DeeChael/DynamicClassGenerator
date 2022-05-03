package net.deechael.dcg;

public enum DigitOperator {

    PLUS("+", true),
    MINUS("-", true),
    MULTIPLY("*", true),
    DIVIDE("/", true),
    REMINDER("%", true),
    BITWISE_NOT("~", false),
    BITWISE_AND("&", true),
    BITWISE_OR("|", true),
    BITWISE_XOR("^", true),
    BITWISE_LEFT_SHIFT("<<", true),
    BITWISE_RIGHT_SHIFT(">>", true),
    BITWISE_RIGHT_LOGICAL_SHIFT(">>>", true),
    INCREASE("++", false),
    DECREASE("--", false);

    private final String symbolString;
    private final boolean isMath;

    DigitOperator(String symbolString, boolean isMath) {
        this.symbolString = symbolString;
        this.isMath = isMath;
    }

    public boolean isMath() {
        return isMath;
    }

    public String getSymbol() {
        return symbolString;
    }

    public String getString() {
        return symbolString;
    }

}
