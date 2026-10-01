package org.ioopm.calculator.ast;

//import org.ioopm.calculator.parser.IllegalAssignmentException;

/**
 * Represents variable assignments in the calculator.
 * Assignments bind a value or expression to a variable.
 */
public class Assignment extends Binary {

    public Assignment(SymbolicExpression lhs, SymbolicExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public String getName() {
        return "=";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public String toString() {
        return getLhs() + " " + getName() + " " + getRhs();
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    /*
     * @Override
     * public SymbolicExpression eval(Environment env) {
     * 
     * SymbolicExpression lhsValue = getLhs().eval(env);
     * 
     * // Validate that rhs is a Variable
     * if (!(getRhs() instanceof Variable)) {
     * throw new
     * RuntimeException("The right-hand side of an assignment must be a variable");
     * }
     * 
     * // Prevent reassignment of named constants
     * if (lhsValue instanceof NamedConstant) {
     * NamedConstant nc = (NamedConstant) lhsValue;
     * throw new
     * IllegalAssignmentException("Error: cannot redefine named constant '" +
     * nc.getName() + "'");
     * }
     * 
     * // Bind lhs value to the variable
     * Variable variable = (Variable) getRhs();
     * env.put(variable, lhsValue);
     * return lhsValue;
     * }
     */
}