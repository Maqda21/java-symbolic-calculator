package org.ioopm.calculator.ast;

/**
 * Abstract superclass for unary operations.
 * Represents operations that require a single operand (e.g., sin, cos, log, -).
 */
public abstract class Unary extends SymbolicExpression {
    private SymbolicExpression operand;

    public Unary(SymbolicExpression operand) {
        this.operand = operand;
    }

    public SymbolicExpression getOperand() {
        return this.operand;
    }

    public String toString() {
        return this.getName() + "" + this.getOperand();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Unary) {
            return this.equals(((Unary) other).operand);
        }
        return false;
    }

    public boolean equals(Unary other) {
        return this.getClass().equals(other.getClass())
            && this.operand.equals(other.operand);
    }
}
