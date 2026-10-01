package org.ioopm.calculator.ast;

public class Log extends Unary {

    public Log(SymbolicExpression operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "log(" + getOperand() + ")";
    }

    @Override
    public String getName() {
        return "log";
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
        Log log = (Log) obj;
        return getOperand().equals(log.getOperand());
    }

    /*
     * @Override
     * public SymbolicExpression eval(Environment env) {
     * SymbolicExpression arg = this.getOperand().eval(env);
     * if (arg.isConstant()) {
     * return new Constant(Math.log(arg.getValue()));
     * } else {
     * return new Log(arg);
     * }
     * }
     */
}
