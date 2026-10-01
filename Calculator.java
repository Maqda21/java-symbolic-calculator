package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Calculator {
    public static void main(String[] args) {
        Environment env = new Environment();
        CalculatorParser parser = new CalculatorParser();
        EvaluationVisitor evaluator = new EvaluationVisitor(env);
        ReassignmentChecker reassignmentChecker = new ReassignmentChecker();
        NamedConstantChecker namedConstantChecker = new NamedConstantChecker();
        boolean inFunctionMode = false;
        FunctionDeclaration currentFunction = null;
        ArrayList<SymbolicExpression> functionBody = new ArrayList<>();

        int totalExpressions = 0;
        int successfullyEvaluated = 0;
        int fullyEvaluated = 0;

        System.out.println("Welcome to the Calculator! Enter expressions or function definitions.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("? ");
                String line = scanner.nextLine().trim();
                try {
                    totalExpressions++;
                    SymbolicExpression expr = parser.parse(line, env);
                    System.out.println("Parsed: " + expr);

                    if (inFunctionMode) {
                        if (expr instanceof End) {
                            inFunctionMode = false;
                            currentFunction.setFunctionBody(new Sequence(functionBody));
                            reassignmentChecker.reset();
                            namedConstantChecker.reset();
                            currentFunction.accept(reassignmentChecker);
                            currentFunction.accept(namedConstantChecker);
                            if (reassignmentChecker.hasErrors()) {
                                System.out.println("Function definition rejected due to reassignment errors.");
                            } else if (!namedConstantChecker.getIllegalAssignments().isEmpty()) {
                                System.out.println("Function definition rejected due to illegal assignments: " +
                                        namedConstantChecker.getIllegalAssignments());
                            } else {
                                env.put(new Variable(currentFunction.getFunctionName()), currentFunction);
                                System.out.println("Function '" + currentFunction.getFunctionName() + "' defined.");
                                successfullyEvaluated++;
                                fullyEvaluated++;
                            }
                            currentFunction = null;
                            functionBody.clear();
                        } else if (expr instanceof FunctionDeclaration) {
                            throw new RuntimeException("Nested function definitions are not permitted.");
                        } else {
                            functionBody.add(expr);
                        }
                    } else {
                        if (expr instanceof FunctionDeclaration) {
                            inFunctionMode = true;
                            currentFunction = (FunctionDeclaration) expr;
                        } else if (expr instanceof End) {
                            throw new RuntimeException(
                                    "Error: 'end' can only occur at the end of a function definition.");
                        } else {
                            reassignmentChecker.reset();
                            namedConstantChecker.reset();
                            expr.accept(reassignmentChecker);
                            expr.accept(namedConstantChecker);
                            if (reassignmentChecker.hasErrors()) {
                                System.out.println("Expression rejected due to reassignment errors.");
                            } else if (!namedConstantChecker.getIllegalAssignments().isEmpty()) {
                                System.out.println("Expression rejected due to illegal assignments: " +
                                        namedConstantChecker.getIllegalAssignments());
                            } else {
                                SymbolicExpression result = evaluator.evaluate(expr, env);
                                System.out.println("Evaluated to: " + result);
                                if (result != null && !expr.isCommand()) {
                                    System.out.println(result);
                                    if (result instanceof Constant) {
                                        fullyEvaluated++;
                                    }
                                }
                                if (expr instanceof Quit) {
                                    System.out.println("Goodbye!");
                                    System.out.println("Statistics:");
                                    System.out.println("    Total Expressions Entered: " + totalExpressions);
                                    System.out.println("    Successfully Evaluated: " + successfullyEvaluated);
                                    System.out.println("    Fully Evaluated: " + fullyEvaluated);
                                    return;
                                }
                            }
                            successfullyEvaluated++;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("IO Error: " + e.getMessage());
                } catch (RuntimeException e) {
                    System.out.println("Runtime Error: " + e.getMessage());
                }
            }
        }
    }
}