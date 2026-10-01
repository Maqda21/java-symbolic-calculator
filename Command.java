package org.ioopm.calculator.ast;

public abstract class Command extends SymbolicExpression {
    
/*     @Override
    public SymbolicExpression eval(Environment env) {
        throw new UnsupportedOperationException("Commands cannot be evaluated");
    } */

    public boolean isCommand() {
        return true;
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
