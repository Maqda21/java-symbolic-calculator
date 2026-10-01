package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;

/**
 * Handles the execution of commands in the calculator (e.g., vars, clear,
 * quit).
 */
public class CommandHandler {

    public static void handleCommand(SymbolicExpression command, Environment env) {
        if (command instanceof Quit) {
            System.out.println("Exiting...");
        } else if (command instanceof Vars) {
            if (env.isEmpty()) {
                System.out.println("No variables defined.");
            } else {
                System.out.println(env);
            }
        } else if (command instanceof Clear) {
            env.clearCurrentScope();
            System.out.println("All variables cleared.");
        } else {
            throw new RuntimeException("Unhandled command: " + command);
        }
    }
}
