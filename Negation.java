package org.ioopm.calculator.ast;

public class Negation extends Unary {

    public Negation(SymbolicExpression operand) {
        super(operand);
    }

    @Override
    public String toString() {
        if (this.getOperand().getPriority() > this.getPriority()) {
            return "-" + this.getOperand();
        } else {
            return "-(" + this.getOperand() + ")";
        }
    }

    @Override
    public String getName() {
        return "neg";
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
     * return new Constant(-arg.getValue());
     * } else {
     * return new Negation(arg);
     * }
     * }
     */

}