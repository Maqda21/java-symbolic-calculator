package org.ioopm.calculator.ast;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ReassignmentChecker implements Visitor {
    private final Stack<Set<String>> assignedVariablesStack = new Stack<>();
    private boolean hasReassignmentError = false;

    public ReassignmentChecker() {
        assignedVariablesStack.push(new HashSet<>()); // Initial global scope
    }

    @Override
    public SymbolicExpression visit(Conditional expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);
        expr.getIfExpression().accept(this);
        expr.getElseExpression().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Assignment expr) {
        if (!(expr.getRhs() instanceof Variable)) {
            throw new RuntimeException("Right-hand side of assignment must be a Variable");
        }
        Variable variable = (Variable) expr.getRhs();
        String variableName = variable.toString(); // Use getName() if available
        Set<String> currentScope = assignedVariablesStack.peek();
        if (currentScope.contains(variableName)) {
            System.out.println("Error: The variable " + variableName + " is reassigned.");
            hasReassignmentError = true;
        } else {
            currentScope.add(variableName);
        }
        expr.getLhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Addition expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Subtraction expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Multiplication expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Division expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Negation expr) {
        expr.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Constant expr) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Variable expr) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Sin expr) {
        expr.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Cos expr) {
        expr.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Exp expr) {
        expr.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Log expr) {
        expr.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Clear expr) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Quit expr) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Vars expr) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Scope scope) {
        assignedVariablesStack.push(new HashSet<>()); // New scope
        scope.getExpression().accept(this); // Check the expression
        assignedVariablesStack.pop(); // Exit scope
        return null;
    }

    @Override
    public SymbolicExpression visit(FunctionDeclaration declaration) {
        if (declaration.getFunctionBody() != null) {
            declaration.getFunctionBody().accept(this);
        }
        return null;
    }

    @Override
    public SymbolicExpression visit(FunctionCall call) {
        for (SymbolicExpression arg : call.getArgs()) {
            arg.accept(this);
        }
        return null;
    }

    @Override
    public SymbolicExpression visit(Sequence sequence) {
        for (SymbolicExpression expr : sequence.getExpressionsList()) {
            expr.accept(this);
        }
        return null;
    }

    @Override
    public SymbolicExpression visit(End end) {
        return null;
    }

    public boolean hasErrors() {
        return hasReassignmentError;
    }

    public void reset() {
        assignedVariablesStack.clear();
        assignedVariablesStack.push(new HashSet<>()); // Reset to global scope
        hasReassignmentError = false;
    }
}