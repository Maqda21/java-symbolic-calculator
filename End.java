package org.ioopm.calculator.ast;

public class End extends SymbolicExpression {
    private static final End instance = new End();

    private End() {}

    public static End instance() {
        return instance;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public String toString() {
        return "end";
    }
}