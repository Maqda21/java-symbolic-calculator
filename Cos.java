package org.ioopm.calculator.ast;

public class Cos extends Unary {

    public Cos(SymbolicExpression operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "cos(" + getOperand() + ")";
    }

    @Override
    public String getName() {
        return "cos";
    }

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Cos cos = (Cos) obj;
        return getOperand().equals(cos.getOperand());
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
     * return new Constant(Math.cos(arg.getValue()));
     * } else {
     * return new Cos(arg);
     * }
     */

}
