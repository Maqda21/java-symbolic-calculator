package org.ioopm.calculator.ast;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents the 'Vars' command which lists all variables.
 */
public class Vars extends Command {
    private static final Vars theInstance = new Vars();

    // This would be a map to hold the variables and their values
    private static Map<String, Double> variables = new HashMap<>();

    private Vars() {
    }

    public String getName() {
        return "vars";
    }

    public String toString() {
        return this.getName();
    }

    public static Vars instance() {
        return theInstance;
    }

    /**
     * Adds a variable to the list of variables.
     * 
     * @param name  The name of the variable.
     * @param value The value of the variable.
     */
    public static void addVariable(String name, Double value) {
        variables.put(name, value);
    }

    /**
     * Lists all variables in the system.
     */
    public String listVariables() {
        if (variables.isEmpty()) {
            return "No variables set.";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : variables.entrySet()) {
            sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Evaluates the 'vars' command by returning a string that lists all the
     * variables.
     * 
     * @param env The environment containing the variables.
     * @return A string representation of the list of variables in the environment.
     */

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean isCommand() {
        return true;
    }

}
