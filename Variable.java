package org.ioopm.calculator.ast;

public class Variable extends Atom implements Comparable<Variable> {
    private String identifier;

    public Variable(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return this.identifier;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Variable) {
            return this.identifier.equals(((Variable) other).identifier);
        }
        return false;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public int compareTo(Variable other) {
        return this.identifier.compareTo(other.identifier);
    }

    @Override
    public String getName() {
        return this.identifier;
    }
}
