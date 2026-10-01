package org.ioopm.calculator.ast;

import java.util.ArrayList;

public class FunctionDeclaration extends SymbolicExpression {
    private String functionName;
    private ArrayList<Variable> parameters;
    private Sequence functionBody;

    public FunctionDeclaration(String functionName, ArrayList<Variable> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.functionBody = null; // Initialized later
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    public Sequence getFunctionBody() {
        return this.functionBody;
    }

    public void setFunctionBody(Sequence functionBody) {
        this.functionBody = functionBody;
    }

    public String getName() {
        return this.getFunctionName();
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public ArrayList<Variable> getParameters() {
        return this.parameters;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FunctionDeclaration) {
            return this.equals((FunctionDeclaration) other);
        }
        return false;
    }

    public boolean equals(FunctionDeclaration other) {
        return this.functionName.equals(other.getFunctionName());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("function ").append(this.functionName).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            result.append(parameters.get(i).getName());
            if (i < parameters.size() - 1) {
                result.append(", ");
            }
        }
        result.append(")\n");
        if (functionBody != null) {
            for (SymbolicExpression expr : functionBody.getExpressionsList()) {
                result.append("  ").append(expr).append("\n");
            }
        }
        result.append("end");
        return result.toString();
    }
}