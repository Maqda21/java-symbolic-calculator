package org.ioopm.calculator.ast;

import java.util.ArrayList;

public class Sequence extends SymbolicExpression {
    private ArrayList<SymbolicExpression> expressions;

    public Sequence(ArrayList<SymbolicExpression> expressions) {
        this.expressions = new ArrayList<>(expressions); // Create a copy of the list
    }

    public ArrayList<SymbolicExpression> getExpressionsList() {
        return this.expressions;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (SymbolicExpression expr : expressions) {
            sb.append(expr).append("\n");
        }
        return sb.toString().trim();
    }
}