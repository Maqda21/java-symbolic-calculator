package org.ioopm.calculator.ast;

/**
 * Represents the 'Clear' command which clears all variables.
 */
public class Clear extends Command {
    private static final Clear theInstance = new Clear();

    private Clear() {
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static Clear instance() {
        return theInstance;
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}