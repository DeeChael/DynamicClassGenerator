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
        if (operator.isMath()) {
            return "(" + first.varString() + " " + operator.getSymbol() + " " + second.varString() + ")";
        } else if (operator == DigitOperator.BITWISE_NOT) {
            return "(~" + first.varString() + ")";
        } else if (operator == DigitOperator.INCREASE || operator == DigitOperator.DECREASE) {
            return first != null ? "(" + first.varString() + "++)" : (second != null ? "(++" + second.varString() + ")" : "0");
        } else {
            throw new RuntimeException("HOW!!!");
        }
    }

}
