package org.ioopm.calculator.ast;

public class Exp extends Unary {

    public Exp(SymbolicExpression operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "exp(" + getOperand() + ")";
    }

    @Override
    public String getName() {
        return "exp";
    }

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    /*
     * @Override
     * public SymbolicExpression eval(Environment env) {
     * SymbolicExpression arg = this.getOperand().eval(env);
     * if (arg.isConstant()) {
     * return new Constant(Math.exp(arg.getValue()));
     * } else {
     * return new Exp(arg);
     * }
     * }
     */
}