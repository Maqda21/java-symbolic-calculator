package org.ioopm.calculator.ast;

import java.util.ArrayList;

public class FunctionCall extends SymbolicExpression {
    private String identifier;
    private ArrayList<SymbolicExpression> arguments;

    public FunctionCall(String identifier, ArrayList<SymbolicExpression> arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    public ArrayList<SymbolicExpression> getArgs() {
        return this.arguments;
    }

    public String getName() {
        return this.identifier;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(identifier).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            sb.append(arguments.get(i));
            if (i < arguments.size() - 1)
                sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}