package org.ioopm.calculator.ast;

/**
 * Abstract superclass for all nodes in the AST. Provides common methods for all
 * symbolic expressions.
 */
public abstract class SymbolicExpression {

    public abstract SymbolicExpression accept(Visitor v);

    /**
     * Evaluates the symbolic expression.
     *
     * @param env The environment containing variable bindings.
     * @return The evaluated result as a SymbolicExpression.
     */
    // public abstract SymbolicExpression eval(Environment env);
    // public abstract SymbolicExpression.eval(Environment)
    public String getName() {
        throw new RuntimeException("getName() called on expression with no operator");
    }

    public boolean isConstant() {
        return false;
    }

    /**
     * Returns the priority of the expression for printing.
     *
     * @return The priority as an integer.
     */
    public int getPriority() {
        return 0;
    }

    public double getValue() {
        throw new RuntimeException("getValue() called on non-constant expression");
    }

    @Override
    public boolean equals(Object other) {
        return this == other;
    }

    /**
     * Checks whether the symbolic expression is a command.
     *
     * @return True if the expression is a command, false otherwise.
     */
    public boolean isCommand() {
        return false;
    }

}
