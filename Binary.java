package org.ioopm.calculator.ast;

/**
 * Abstract superclass for binary operations.
 * Represents operations that require two operands (e.g., +, -, *, /).
 */
public abstract class Binary extends SymbolicExpression {
    private SymbolicExpression lhs;
    private SymbolicExpression rhs;

    public Binary(SymbolicExpression lhs, SymbolicExpression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public SymbolicExpression getLhs() {
        return this.lhs;
    }

    public SymbolicExpression getRhs() {
        return this.rhs;
    }

    @Override
    public String toString() {
        int lhsPriority = lhs.getPriority();
        int rhsPriority = rhs.getPriority();

        if (lhsPriority < rhsPriority) {
            return "(" + lhs.toString() + ")" + " " + getName() + " " + rhs.toString();
        } else if (rhsPriority < lhsPriority) {
            return lhs.toString() + " " + getName() + " " + "(" + rhs.toString() + ")";
        } 
        return this.lhs.toString() + " " + this.getName() + " " + this.rhs.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Binary) {
            return this.equals((Binary) other);
        }
        return false;
    }

    public boolean equals(Binary other) {
        return this.getClass().equals(other.getClass())
            && this.lhs.equals(other.lhs)
            && this.rhs.equals(other.rhs);
    }

}
