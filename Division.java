package org.ioopm.calculator.ast;

public class Division extends Binary {

    public Division(SymbolicExpression lhs, SymbolicExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public String toString() {
        return "(" + getLhs() + " / " + getRhs() + ")";
    }

    @Override
    public String getName() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    /*
     * @Override
     * public SymbolicExpression eval(Environment env) {
     * SymbolicExpression left = getLhs().eval(env);
     * SymbolicExpression right = getRhs().eval(env);
     * 
     * if (left.isConstant() && right.isConstant()) {
     * return new Constant(left.getValue() / right.getValue());
     * }
     * return new Division(left, right);
     * }
     */
}