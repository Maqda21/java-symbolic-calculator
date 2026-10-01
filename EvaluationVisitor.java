package org.ioopm.calculator.ast;

import java.util.ArrayList;
import java.util.List;

public class EvaluationVisitor implements Visitor {
    private Environment env;
    private List<String> illegalAssignments = new ArrayList<>();

    public EvaluationVisitor(Environment env) {
        this.env = env;
    }

    public SymbolicExpression evaluate(SymbolicExpression topLevel, Environment env) {
        this.env = env;
        return topLevel.accept(this);
    }

    private SymbolicExpression evaluateFully(SymbolicExpression expr) {
        SymbolicExpression result = expr.accept(this);
        if (result instanceof Variable) {
            Variable var = (Variable) result;
            SymbolicExpression value = env.get(var);
            if (value != null) {
                return evaluateFully(value); // Recursively resolve to the latest value
            }
            return result; // Return variable if unbound
        } else if (result instanceof Assignment) {
            return evaluateFully(((Assignment) result).getLhs());
        } else if (result instanceof Addition) {
            Addition add = (Addition) result;
            SymbolicExpression left = evaluateFully(add.getLhs());
            SymbolicExpression right = evaluateFully(add.getRhs());
            if (left instanceof Constant && right instanceof Constant) {
                return new Constant(((Constant) left).getValue() + ((Constant) right).getValue());
            }
            return new Addition(left, right);
        }
        return result;
    }

    @Override
    public SymbolicExpression visit(Addition n) {
        SymbolicExpression left = n.getLhs().accept(this); // Evaluate left side
        SymbolicExpression right = n.getRhs().accept(this); // Evaluate right side independently
        // Do not call evaluateFully yet, let the results stand as-is
        if (left instanceof Constant && right instanceof Constant) {
            return new Constant(((Constant) left).getValue() + ((Constant) right).getValue());
        }
        return new Addition(left, right); // Return symbolic if either side is not a constant
    }

    @Override
    public SymbolicExpression visit(Assignment a) {
        SymbolicExpression value = a.getLhs().accept(this); // LHS is the value (e.g., 4)
        if (!(a.getRhs() instanceof Variable)) {
            throw new RuntimeException("Assignment target must be a variable on the right side");
        }
        env.put((Variable) a.getRhs(), value); // Assign value to the variable on RHS
        return value; // Return the assigned value
    }

    @Override
    public SymbolicExpression visit(Clear clear) {
        env.clearCurrentScope();
        return clear;
    }

    @Override
    public SymbolicExpression visit(Constant constant) {
        return constant;
    }

    @Override
    public SymbolicExpression visit(Cos cos) {
        SymbolicExpression arg = evaluateFully(cos.getOperand());
        if (arg instanceof Constant) {
            return new Constant(Math.cos(((Constant) arg).getValue()));
        }
        return new Cos(arg);
    }

    @Override
    public SymbolicExpression visit(Division division) {
        SymbolicExpression left = evaluateFully(division.getLhs());
        SymbolicExpression right = evaluateFully(division.getRhs());
        if (left instanceof Constant && right instanceof Constant) {
            if (((Constant) right).getValue() == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return new Constant(((Constant) left).getValue() / ((Constant) right).getValue());
        }
        return new Division(left, right);
    }

    @Override
    public SymbolicExpression visit(Exp exp) {
        SymbolicExpression arg = evaluateFully(exp.getOperand());
        if (arg instanceof Constant) {
            return new Constant(Math.exp(((Constant) arg).getValue()));
        }
        return new Exp(arg);
    }

    @Override
    public SymbolicExpression visit(Log log) {
        SymbolicExpression arg = evaluateFully(log.getOperand());
        if (arg instanceof Constant) {
            if (((Constant) arg).getValue() <= 0) {
                throw new ArithmeticException("Logarithm of non-positive number");
            }
            return new Constant(Math.log(((Constant) arg).getValue()));
        }
        return new Log(arg);
    }

    @Override
    public SymbolicExpression visit(Multiplication multiplication) {
        SymbolicExpression left = evaluateFully(multiplication.getLhs());
        SymbolicExpression right = evaluateFully(multiplication.getRhs());
        if (left instanceof Constant && right instanceof Constant) {
            return new Constant(((Constant) left).getValue() * ((Constant) right).getValue());
        }
        return new Multiplication(left, right);
    }

    @Override
    public SymbolicExpression visit(Negation negation) {
        SymbolicExpression arg = evaluateFully(negation.getOperand());
        if (arg instanceof Constant) {
            return new Constant(-((Constant) arg).getValue());
        }
        return new Negation(arg);
    }

    @Override
    public SymbolicExpression visit(Quit quit) {
        return quit;
    }

    @Override
    public SymbolicExpression visit(Sin sin) {
        SymbolicExpression arg = evaluateFully(sin.getOperand());
        if (arg instanceof Constant) {
            return new Constant(Math.sin(((Constant) arg).getValue()));
        }
        return new Sin(arg);
    }

    @Override
    public SymbolicExpression visit(Subtraction subtraction) {
        SymbolicExpression left = evaluateFully(subtraction.getLhs());
        SymbolicExpression right = evaluateFully(subtraction.getRhs());
        if (left instanceof Constant && right instanceof Constant) {
            return new Constant(((Constant) left).getValue() - ((Constant) right).getValue());
        }
        return new Subtraction(left, right);
    }

    @Override
    public SymbolicExpression visit(Variable v) {
        String varName = v.getName();
        if (Constants.namedConstants.containsKey(varName)) {
            return new Constant(Constants.namedConstants.get(varName));
        }
        SymbolicExpression value = env.get(v);
        if (value != null) {
            return value;
        }
        return v;
    }

    @Override
    public SymbolicExpression visit(Vars vars) {
        StringBuilder variableList = new StringBuilder("Variables: ");
        for (Variable var : env.keySet()) {
            variableList.append(var.getName()).append(" = ").append(env.get(var)).append(" ");
        }
        System.out.println(variableList.toString().trim());
        return null;
    }

    @Override
    public SymbolicExpression visit(Conditional conditional) {
        SymbolicExpression lhs = evaluateFully(conditional.getLhs());
        SymbolicExpression rhs = evaluateFully(conditional.getRhs());
        if (!(lhs instanceof Constant && rhs instanceof Constant)) {
            throw new RuntimeException("Free variables are not supported in conditionals");
        }
        double lhsValue = ((Constant) lhs).getValue();
        double rhsValue = ((Constant) rhs).getValue();
        boolean condition;
        switch (conditional.getOperation()) {
            case ">":
                condition = lhsValue > rhsValue;
                break;
            case "<":
                condition = lhsValue < rhsValue;
                break;
            case ">=":
                condition = lhsValue >= rhsValue;
                break;
            case "<=":
                condition = lhsValue <= rhsValue;
                break;
            case "==":
                condition = lhsValue == rhsValue;
                break;
            default:
                throw new RuntimeException("Conditional operation " + conditional.getOperation() + " not supported");
        }
        if (condition) {
            return evaluateFully(conditional.getIfExpression().accept(this));
        } else {
            return evaluateFully(conditional.getElseExpression().accept(this));
        }
    }

    @Override
    public SymbolicExpression visit(Scope s) {
        Environment localEnv = new Environment();
        for (Variable key : env.keySet()) {
            localEnv.put(key, env.get(key));
        }
        EvaluationVisitor localEvaluator = new EvaluationVisitor(localEnv);
        SymbolicExpression result = s.getExpression().accept(localEvaluator);

        if (result instanceof Variable
                && !(s.getExpression() instanceof Scope || (s.getExpression() instanceof Assignment
                        && ((Assignment) s.getExpression()).getLhs() instanceof Scope))) {
            return result;
        }
        return localEvaluator.evaluateFully(result);
    }

    @Override
    public SymbolicExpression visit(FunctionDeclaration declaration) {
        env.put(new Variable(declaration.getFunctionName()), declaration);
        return declaration;
    }

    @Override
    public SymbolicExpression visit(FunctionCall call) {
        SymbolicExpression funcExpr = env.get(new Variable(call.getName()));
        if (!(funcExpr instanceof FunctionDeclaration)) {
            throw new RuntimeException("Function '" + call.getName() + "' is not defined");
        }
        FunctionDeclaration func = (FunctionDeclaration) funcExpr;

        ArrayList<Variable> params = func.getParameters();
        ArrayList<SymbolicExpression> args = call.getArgs();
        if (args.size() != params.size()) {
            throw new RuntimeException(
                    "Error, function '" + call.getName() + "' called with wrong number of arguments. Expected " +
                            params.size() + ", got " + args.size());
        }

        env.pushEnvironment();
        for (int i = 0; i < args.size(); i++) {
            SymbolicExpression argValue = args.get(i).accept(this);
            if (!(argValue instanceof Constant || argValue instanceof Variable)) {
                throw new RuntimeException("Function arguments must be numbers or identifiers");
            }
            visit(new Assignment(argValue, params.get(i)));
        }

        SymbolicExpression result = func.getFunctionBody().accept(this);
        env.popEnvironment();
        return result;
    }

    @Override
    public SymbolicExpression visit(Sequence sequence) {
        SymbolicExpression result = null;
        for (SymbolicExpression expr : sequence.getExpressionsList()) {
            result = expr.accept(this);
        }
        return result;
    }

    @Override
    public SymbolicExpression visit(End end) {
        throw new RuntimeException("End should not be evaluated directly");
    }

    public boolean check(SymbolicExpression expr) {
        illegalAssignments.clear();
        expr.accept(this);
        return illegalAssignments.isEmpty();
    }
}