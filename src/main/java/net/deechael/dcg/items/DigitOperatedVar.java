package net.deechael.dcg.items;

import net.deechael.dcg.DigitOperator;

final class DigitOperatedVar implements Var {

    private final Var first;
    private final Var second;

    private final DigitOperator operator;

    public DigitOperatedVar(Var first, DigitOperator operator, Var second) {
        this.first = first;
        this.second = second;

        this.operator = operator;
    }

    @Override
    public Class<?> getType() {
        return int.class;
    }

    @Override
    public String getName() {
        throw new RuntimeException("Digit has no name");
    }

    @Override
    public String varString() {
        switch (operator) {
            case PLUS:
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
            case REMINDER:
            case BITWISE_OR:
            case BITWISE_AND:
            case BITWISE_XOR:
            case BITWISE_LEFT_SHIFT:
            case BITWISE_RIGHT_SHIFT:
            case BITWISE_RIGHT_LOGICAL_SHIFT:
                return "(" + first.varString() + " " + operator.getSymbol() + " " + second.varString() + ")";
            case BITWISE_NOT:
                return "(~" + first.varString() + ")";
            case INCREASE:
            case DECREASE:
                if (first != null) {
                    return "(" + first.varString() + "++)";
                } else if (second != null) {
                    return "(++" + first.varString() + ")";
                } else {
                    return "0";
                }
        }
        throw new RuntimeException("Failed to operate!");
    }

}
