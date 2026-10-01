package org.ioopm.calculator.ast;

public class Sin extends Unary {

    public Sin(SymbolicExpression operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "sin(" + getOperand() + ")";
    }

    @Override
    public String getName() {
        return "sin";
    }

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Sin sin = (Sin) obj;
        return getOperand().equals(sin.getOperand());
    }

    /*
     * @Override
     * public SymbolicExpression eval(Environment env) {
     * SymbolicExpression arg = this.getOperand().eval(env);
     * if (arg.isConstant()) {
     * return new Constant(Math.sin(arg.getValue()));
     * } else {
     * return new Sin(arg);
     * }
     * }
     */
}
