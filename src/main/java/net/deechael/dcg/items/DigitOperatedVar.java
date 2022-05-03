package net.deechael.dcg.items;

import net.deechael.dcg.DigitOperator;
import org.jetbrains.annotations.NotNull;

final class DigitOperatedVar implements Var {

    private final Var first;
    private final Var second;

    private final DigitOperator operator;

    public DigitOperatedVar(Var first, @NotNull DigitOperator operator, Var second) {
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
        if (operator == DigitOperator.PLUS || operator == DigitOperator.MINUS || operator == DigitOperator.MULTIPLY || operator == DigitOperator.DIVIDE || operator == DigitOperator.REMINDER || operator == DigitOperator.BITWISE_OR || operator == DigitOperator.BITWISE_AND || operator == DigitOperator.BITWISE_XOR || operator == DigitOperator.BITWISE_LEFT_SHIFT || operator == DigitOperator.BITWISE_RIGHT_SHIFT || operator == DigitOperator.BITWISE_RIGHT_LOGICAL_SHIFT) {
            return "(" + first.varString() + " " + operator.getSymbol() + " " + second.varString() + ")";
        } else if (operator == DigitOperator.BITWISE_NOT) {
            return "(~" + first.varString() + ")";
        } else if (operator == DigitOperator.INCREASE || operator == DigitOperator.DECREASE) {
            if (first != null) {
                return "(" + first.varString() + "++)";
            } else if (second != null) {
                return "(++" + first.varString() + ")";
            } else {
                return "0";
            }
        } else {
            throw new RuntimeException("HOW!!!");
        }
    }

}
