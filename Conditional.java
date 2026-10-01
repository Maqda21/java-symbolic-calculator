package org.ioopm.calculator.ast;

public class Conditional extends SymbolicExpression {
    private String operation;
    private SymbolicExpression lhs;
    private SymbolicExpression rhs;
    private SymbolicExpression ifExpression;
    private SymbolicExpression elseExpression;

    public Conditional(String operation, SymbolicExpression lhs, SymbolicExpression rhs,
            SymbolicExpression ifExpression, SymbolicExpression elseExpression) {
        this.operation = operation;
        this.lhs = lhs;
        this.rhs = rhs;
        this.ifExpression = ifExpression;
        this.elseExpression = elseExpression;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    public String getOperation() {
        return operation;
    }

    public SymbolicExpression getLhs() {
        return lhs;
    }

    public SymbolicExpression getRhs() {
        return rhs;
    }

    public SymbolicExpression getIfExpression() {
        return ifExpression;
    }

    public SymbolicExpression getElseExpression() {
        return elseExpression;
    }

    @Override
    public String toString() {
        return "if " + lhs + " " + operation + " " + rhs + " " + ifExpression + " else " + elseExpression;
    }

    @Override
    public int getPriority() {
        return 10; // Low priority for conditionals to ensure proper parenthesization
    }
}