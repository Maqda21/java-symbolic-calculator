package org.ioopm.calculator.ast;

public interface Visitor {
    SymbolicExpression visit(Addition n);

    SymbolicExpression visit(Assignment assignment);

    SymbolicExpression visit(Clear clear);

    SymbolicExpression visit(Constant constant);

    SymbolicExpression visit(Cos cos);

    SymbolicExpression visit(Division division);

    SymbolicExpression visit(Exp exp);

    SymbolicExpression visit(Log log);

    SymbolicExpression visit(Multiplication multiplication);

    SymbolicExpression visit(Negation negation);

    SymbolicExpression visit(Quit quit);

    SymbolicExpression visit(Sin sin);

    SymbolicExpression visit(Subtraction subtraction);

    SymbolicExpression visit(Variable variable);

    SymbolicExpression visit(Vars vars);

    SymbolicExpression visit(Scope scope);

    SymbolicExpression visit(Conditional conditional);

    SymbolicExpression visit(FunctionDeclaration declaration); // New

    SymbolicExpression visit(FunctionCall call); // New

    SymbolicExpression visit(Sequence sequence); // New

    SymbolicExpression visit(End end); // New
}