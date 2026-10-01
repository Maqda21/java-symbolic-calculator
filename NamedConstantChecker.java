package org.ioopm.calculator.ast;

import java.util.ArrayList;
import java.util.List;

public class NamedConstantChecker implements Visitor {
    private List<String> illegalAssignments = new ArrayList<>();

    public SymbolicExpression visit(Addition a) {
        a.getLhs().accept(this);
        a.getRhs().accept(this);
        return null;
    }

    public SymbolicExpression visit(Assignment a) {
        a.getLhs().accept(this);
        a.getRhs().accept(this);
        if (a.getRhs() instanceof Constant) {
            Constant rhs = (Constant) a.getRhs();
            if (rhs.getName() != null && !rhs.getName().isEmpty()) {
                illegalAssignments.add(a.getLhs() + " = " + a.getRhs());
            }
        }
        return null;
    }

    public SymbolicExpression visit(Clear n) {
        return null;
    }

    public SymbolicExpression visit(Constant n) {
        return null;
    }

    public SymbolicExpression visit(Cos n) {
        n.getOperand().accept(this);
        return null;
    }

    public SymbolicExpression visit(Division n) {
        n.getLhs().accept(this);
        n.getRhs().accept(this);
        return null;
    }

    public SymbolicExpression visit(Exp n) {
        n.getOperand().accept(this);
        return null;
    }

    public SymbolicExpression visit(Log n) {
        n.getOperand().accept(this);
        return null;
    }

    public SymbolicExpression visit(Multiplication n) {
        n.getLhs().accept(this);
        n.getRhs().accept(this);
        return null;
    }

    public SymbolicExpression visit(Negation n) {
        n.getOperand().accept(this);
        return null;
    }

    public SymbolicExpression visit(Quit n) {
        return null;
    }

    public SymbolicExpression visit(Sin n) {
        n.getOperand().accept(this);
        return null;
    }

    public SymbolicExpression visit(Subtraction n) {
        n.getLhs().accept(this);
        n.getRhs().accept(this);
        return null;
    }

    public SymbolicExpression visit(Variable n) {
        return null;
    }

    public SymbolicExpression visit(Vars n) {
        return null;
    }

    public SymbolicExpression visit(Conditional c) {
        c.getLhs().accept(this); // Check the left-hand side of the condition
        c.getRhs().accept(this); // Check the right-hand side of the condition
        c.getIfExpression().accept(this); // Check the if branch
        c.getElseExpression().accept(this); // Check the else branch
        return null;
    }

    public SymbolicExpression visit(Scope scope) {
        scope.getExpression().accept(this); // Process the single expression
        return null; // No meaningful result needed for a checker
    }

    public SymbolicExpression visit(FunctionDeclaration declaration) {
        // Check the function body for illegal assignments
        if (declaration.getFunctionBody() != null) {
            declaration.getFunctionBody().accept(this);
        }
        return null;
    }

    public SymbolicExpression visit(FunctionCall call) {
        // Check each argument for illegal assignments
        for (SymbolicExpression arg : call.getArgs()) {
            arg.accept(this);
        }
        return null;
    }

    public SymbolicExpression visit(Sequence sequence) {
        // Check each expression in the sequence
        for (SymbolicExpression expr : sequence.getExpressionsList()) {
            expr.accept(this);
        }
        return null;
    }

    public SymbolicExpression visit(End end) {
        // End doesn’t contain assignments, so nothing to check
        return null;
    }

    public List<String> getIllegalAssignments() {
        return illegalAssignments;
    }

    // Optional: Add a reset method for consistency with ReassignmentChecker
    public void reset() {
        illegalAssignments.clear();
    }
}