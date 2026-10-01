package org.ioopm.calculator.ast;

import java.util.ArrayList;

/**
 * Represents a Scope in the AST that contains a single expression.
 */
public class Scope extends SymbolicExpression {
    private final SymbolicExpression expression; // Single expression instead of a list
    private final Environment env;

    /**
     * Constructor for Scope.
     * 
     * @param expression The single expression inside the scope.
     */
    public Scope(SymbolicExpression expression) {
        this.expression = expression;
        this.env = new Environment(); // Fresh environment for this scope
    }

    /**
     * Gets the expression contained in this scope.
     * 
     * @return The SymbolicExpression.
     */
    public SymbolicExpression getExpression() {
        return expression;
    }

    public Environment getEnv() {
        return env;
    }

    @Override
    public String toString() {
        return "{" + expression.toString() + "}";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Scope) {
            return this.expression.equals(((Scope) other).expression);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}
