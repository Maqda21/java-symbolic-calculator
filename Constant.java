package org.ioopm.calculator.ast;

public class Constant extends Atom {
    private double value;

    public Constant(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public double getValue() {
        return this.value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Constant) {
            Constant c = (Constant) other;
            return Double.compare(this.value, c.value) == 0;
        }
        return false;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    /*
     * @Override
     * public SymbolicExpression eval(Environment env) {
     * return this;
     * }
     */
}
