package org.ioopm.calculator.ast;

import java.util.HashMap;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Iterator;

import java.util.*;

/**
 * The Environment class manages variable bindings for the calculator.
 * Supports scoping using a stack of environments.
 */
public class Environment {
    private final Stack<HashMap<Variable, SymbolicExpression>> environmentStack;

    /**
     * Creates a new Environment with an initial (global) scope.
     */
    public Environment() {
        this.environmentStack = new Stack<>();
        this.environmentStack.push(new HashMap<>()); // Global scope
    }

    // In the Environment class
    public boolean isEmpty() {
        return this.environmentStack.peek().isEmpty(); // Check if the current (innermost) scope is empty
    }

    /**
     * Pushes a new scope (environment) onto the stack.
     */
    public void pushEnvironment() {
        this.environmentStack.push(new HashMap<>());
    }

    /**
     * Pops the current scope (environment) off the stack.
     * Throws an exception if attempting to pop the global scope.
     */
    public void popEnvironment() {
        if (this.environmentStack.size() > 1) {
            this.environmentStack.pop();
        } else {
            throw new IllegalStateException("Cannot pop the global environment");
        }
    }

    /**
     * Adds or updates a variable binding in the current scope.
     * 
     * @param variable The variable to bind.
     * @param value    The symbolic expression to associate with the variable.
     */
    public void put(Variable variable, SymbolicExpression value) {
        this.environmentStack.peek().put(variable, value);
    }

    /**
     * Retrieves the value associated with a variable.
     * Searches from the innermost scope outward.
     * 
     * @param variable The variable to look up.
     * @return The associated symbolic expression, or null if not found.
     */
    /*
     * public SymbolicExpression get(Variable variable) {
     * for (int i = this.environmentStack.size() - 1; i >= 0; i--) {
     * HashMap<Variable, SymbolicExpression> scope = this.environmentStack.get(i);
     * for (Variable key : scope.keySet()) {
     * if (key.getName().equals(variable.getName())) { // Compare names, not objects
     * return scope.get(key);
     * }
     * }
     * }
     * return null;
     * }
     */

    /**
     * Retrieves the value associated with a variable.
     * Searches from the innermost scope outward.
     * 
     * @param variable The variable to look up.
     * @return The associated symbolic expression, or null if not found.
     */
    public SymbolicExpression get(Variable variable) {
        for (int i = this.environmentStack.size() - 1; i >= 0; i--) {
            HashMap<Variable, SymbolicExpression> scope = this.environmentStack.get(i);
            if (scope.containsKey(variable)) {
                return scope.get(variable);
            }
        }
        return null;
    }

    /**
     * Checks if a variable exists in the environment.
     * Searches from the innermost scope outward.
     * 
     * @param variable The variable to check.
     * @return True if the variable exists, otherwise false.
     */
    public boolean containsKey(Variable variable) {
        for (int i = this.environmentStack.size() - 1; i >= 0; i--) {
            if (this.environmentStack.get(i).containsKey(variable)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all bindings in the current (innermost) scope.
     */
    public void clearCurrentScope() {
        this.environmentStack.peek().clear();
    }

    /**
     * Retrieves all variables in the current environment.
     * 
     * @return A set of variables from all active scopes, ordered lexicographically.
     */
    public TreeSet<Variable> keySet() {
        TreeSet<Variable> keys = new TreeSet<>();
        for (HashMap<Variable, SymbolicExpression> scope : this.environmentStack) {
            keys.addAll(scope.keySet());
        }
        return keys;
    }

    /**
     * Evaluates an expression within the current environment.
     * This is a helper function that can be used if needed.
     * 
     * @param expression The expression to evaluate.
     * @return The result of the evaluation.
     */
    public SymbolicExpression evaluate(SymbolicExpression expression) {
        return expression.accept(new EvaluationVisitor(this));

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Environment:\n");
        for (int i = 0; i < this.environmentStack.size(); i++) {
            sb.append("Scope ").append(i).append(": ");
            TreeSet<Variable> vars = new TreeSet<>(this.environmentStack.get(i).keySet());
            Iterator<Variable> iter = vars.iterator();
            while (iter.hasNext()) {
                Variable v = iter.next();
                sb.append(v.getName()).append(" = ").append(this.environmentStack.get(i).get(v));
                if (iter.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}