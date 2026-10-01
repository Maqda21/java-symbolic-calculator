package org.ioopm.calculator.ast;

/**
 * Represents the 'Quit' command which exits the program.
 */
public class Quit extends Command {
    private static final Quit theInstance = new Quit();

    private Quit() {
    }

    @Override
    public String getName() {
        return "quit";
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static Quit instance() {
        return theInstance;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean isCommand() {
        return true;
    }
}
