package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.ioopm.calculator.parser.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Iterator;

import javax.accessibility.AccessibleRelationSet;

public class TestCalculator {

    private static PrintStream originalOut;
    private static PrintStream fileOut;

    // Redirect System.out to a file before all tests
    @BeforeAll
    static void setup() {
        try {
            originalOut = System.out; // Store original System.out
            fileOut = new PrintStream(new FileOutputStream("test-report.txt"));
            System.setOut(fileOut); // Redirect System.out to test-report.txt
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reset System.out to original after all tests
    @AfterAll
    static void tearDown() {
        System.setOut(originalOut); // Restore original System.out
        if (fileOut != null) {
            fileOut.close(); // Close the file output stream
        }
    }

    @Test
    public void testEvaluating() {
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor(env); // Create an evaluator
        // Example test cases
        SymbolicExpression c = new Constant(42);
        SymbolicExpression add = new Addition(new Constant(5), new Constant(37));
        testEvaluating(c, c, env, evaluator);
        testEvaluating(new Constant(42), add, env, evaluator);
    }

    @Test
    public void testArithmeticExpressions() {
        System.out.println("Testing arithmetic expressions:");

        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor(env); // Create an evaluator

        // Test: Constant
        System.out.println("Testing constant expression:");
        SymbolicExpression c = new Constant(42);
        testEvaluating(c, c, env, evaluator);

        // Test: Addition
        System.out.println("Testing addition expression:");
        SymbolicExpression add = new Addition(new Constant(5), new Constant(37));
        SymbolicExpression result = new Constant(42);
        testEvaluating(result, add, env, evaluator);

        // Test: Multiplication
        System.out.println("Testing multiplication expression:");
        SymbolicExpression mult = new Multiplication(new Constant(6), new Constant(7));
        testEvaluating(new Constant(42), mult, env, evaluator);

        // Test: Complex expression
        System.out.println("Testing complex arithmetic expression:");
        SymbolicExpression complex = new Addition(
                new Multiplication(new Constant(2), new Constant(3)),
                new Subtraction(new Constant(10), new Constant(4)));
        testEvaluating(new Constant(12), complex, env, evaluator);

        // Test: Division
        System.out.println("Testing division expression:");
        SymbolicExpression div = new Division(new Constant(42), new Constant(7));
        SymbolicExpression expectedResult = new Constant(6);
        testEvaluating(expectedResult, div, env, evaluator);

        // Test: Subtraction
        System.out.println("Testing subtraction expression:");
        SymbolicExpression sub = new Subtraction(new Constant(10), new Constant(4));
        SymbolicExpression expectedSubtractionResult = new Constant(6);
        testEvaluating(expectedSubtractionResult, sub, env, evaluator);
    }

    @Test
    public void testTrigonometricExpressions() {
        System.out.println("Testing sine, cosine, and logarithmic expressions:");

        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor(env); // Create an evaluator

        // Test: Cosine of constant (cos(0))
        System.out.println("Testing cosine of constant expression (cos(0)):");
        SymbolicExpression cosOfZero = new Cos(new Constant(0));
        SymbolicExpression expectedCosZero = new Constant(Math.cos(0)); // Expected evaluated value
        testEvaluating(expectedCosZero, cosOfZero, env, evaluator); // Test evaluated result (1.0)

        // Test: Cosine of constant (cos(π/2))
        System.out.println("Testing cosine of constant expression (cos(π/2)):");
        SymbolicExpression cosOfPiOver2 = new Cos(new Constant(Math.PI / 2));
        SymbolicExpression expectedCosPiOver2 = new Constant(Math.cos(Math.PI / 2)); // Expected evaluated value
        testEvaluating(expectedCosPiOver2, cosOfPiOver2, env, evaluator); // Test evaluated result (0.0)

        // Test: Cosine of a variable (cos(x))
        System.out.println("Testing cosine of a variable (cos(x)):");
        Variable varCos = new Variable("x");
        SymbolicExpression cosOfVar = new Cos(varCos);
        testEvaluating(cosOfVar, cosOfVar, env, evaluator); // Test symbolic form

        // Test: Sine of constant (sin(0))
        System.out.println("Testing sine of constant expression (sin(0)):");
        SymbolicExpression sinOfZero = new Sin(new Constant(0));
        SymbolicExpression expectedSinZero = new Constant(Math.sin(0)); // Expected evaluated value
        testEvaluating(expectedSinZero, sinOfZero, env, evaluator); // Test evaluated result (0.0)

        // Test: Sine of constant (sin(π/2))
        System.out.println("Testing sine of constant expression (sin(π/2)):");
        SymbolicExpression sinOfPiOver2 = new Sin(new Constant(Math.PI / 2));
        SymbolicExpression expectedSinPiOver2 = new Constant(Math.sin(Math.PI / 2)); // Expected evaluated value
        testEvaluating(expectedSinPiOver2, sinOfPiOver2, env, evaluator); // Test evaluated result (1.0)

        // Test: Sine of a variable (sin(x))
        System.out.println("Testing sine of a variable (sin(x)):");
        Variable varSin = new Variable("z");
        SymbolicExpression sinOfVar = new Sin(varSin);
        testEvaluating(sinOfVar, sinOfVar, env, evaluator); // Test symbolic form

        // Test: Logarithm of constant (log(1))
        System.out.println("Testing logarithm of constant expression (log(1)):");
        SymbolicExpression logOfOne = new Log(new Constant(1));
        SymbolicExpression expectedLogOfOne = new Constant(Math.log(1)); // Expected evaluated value
        testEvaluating(expectedLogOfOne, logOfOne, env, evaluator); // Test evaluated result (0.0)

        // Test: Logarithm of constant (log(10))
        System.out.println("Testing logarithm of constant expression (log(10)):");
        SymbolicExpression logOfTen = new Log(new Constant(10));
        SymbolicExpression expectedLogOfTen = new Constant(Math.log(10)); // Expected evaluated value
        testEvaluating(expectedLogOfTen, logOfTen, env, evaluator); // Test evaluated result (2.302585)

        // Test: Logarithm of a variable (log(y))
        System.out.println("Testing logarithm of a variable (log(y)):");
        Variable varLog = new Variable("y");
        SymbolicExpression logOfVar = new Log(varLog);
        testEvaluating(logOfVar, logOfVar, env, evaluator); // Test symbolic form
    }

    // Helper function to evaluate and print results
    private void testEvaluating(SymbolicExpression expected, SymbolicExpression e, Environment env,
            EvaluationVisitor evaluator) {
        SymbolicExpression result = evaluator.evaluate(e, env); // Use the visitor to evaluate the expression
        try {
            if (expected.equals(result)) {
                System.out.println("Test passed: " + e + " evaluated correctly.");
            } else {
                System.out.println("Test failed: " + e + " expected " + expected + " but got " + result);
            }
        } catch (Exception ex) {
            System.out.println("Test failed with error: " + ex.getMessage());
        }
        assertEquals(expected, result, "Error: expected '" + expected + "' but got '" + result + "'");
    }

    @Test
    public void testGetValueOnNonConstantExpression() {
        // Create a non-constant expression
        SymbolicExpression nonConstantExpr = new Addition(new Constant(5), new Constant(3));

        // Assert that calling getValue() throws a RuntimeException
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            nonConstantExpr.getValue();
        });

        // Verify the exception message
        assertEquals("getValue() called on non-constant expression", thrown.getMessage());
    }

    @Test
    public void testIsConstantOnNonConstantExpression() {
        // Create a non-constant expression
        SymbolicExpression nonConstantExpr = new Addition(new Constant(5), new Constant(3));

        // Assert that isConstant() returns false
        assertFalse(nonConstantExpr.isConstant());
    }

    @Test
    public void testValidExpressionParsing() throws IOException {
        Environment env = new Environment();
        CalculatorParser parser = new CalculatorParser();

        // Test: Simple addition
        SymbolicExpression result = parser.parse("5 + 3", env);
        assertTrue(result instanceof Addition);

        // Test: Variable assignment
        parser.parse("5 = x", env);
        SymbolicExpression var = parser.parse("x", env);
        assertTrue(var instanceof Variable);
        assertEquals("x", ((Variable) var).getName());

        // Test: Assignment with expression
        parser.parse("2 + 3 = y", env);
        var = parser.parse("y", env);
        assertTrue(var instanceof Variable);
        assertEquals("y", ((Variable) var).getName());
    }

    @Test
    public void testIsCommandOnNonCommandExpression() {
        // Create a non-command expression
        SymbolicExpression nonCommandExpr = new Addition(new Constant(5), new Constant(3));

        // Assert that isCommand() returns false
        assertFalse(nonCommandExpr.isCommand());
    }

    @Test
    public void testGetPriority() {
        SymbolicExpression addition = new Addition(new Constant(5), new Constant(3));
        SymbolicExpression multiplication = new Multiplication(new Constant(2), new Constant(3));

        assertEquals(50, addition.getPriority(), "Addition should return priority 50");
        assertEquals(100, multiplication.getPriority(), "Multiplication should return priority 100");
    }

    @Test
    public void testEqualsMethod() {
        SymbolicExpression expr1 = new Addition(new Constant(5), new Constant(3));
        SymbolicExpression expr2 = new Addition(new Constant(5), new Constant(3));
        SymbolicExpression expr3 = new Multiplication(new Constant(5), new Constant(3));

        // Test: Two equivalent expressions should be equal
        assertTrue(expr1.equals(expr2), "Expressions should be equal");

        // Test: Different expressions should not be equal
        assertFalse(expr1.equals(expr3), "Different expressions should not be equal");

        // Test: Comparing with null should return false
        assertFalse(expr1.equals(null), "Expression should not be equal to null");

        // Test: Comparing different types should return false
        assertFalse(expr1.equals("string"), "Expression should not be equal to an unrelated type");

        // Test: Reflexivity
        assertTrue(expr1.equals(expr1), "Expression should be equal to itself");
    }

    public class TestToString {

        class SampleExpression {

            @Override
            public String toString() {
                return getClass().getName() + "@" + Integer.toHexString(hashCode());
            }
        }

        @Test
        public void testParserWithToString() throws IOException {
            Environment env = new Environment();
            CalculatorParser parser = new CalculatorParser();

            // Create AST nodes programmatically
            SymbolicExpression originalExpr = new Addition(new Constant(42), new Negation(new Constant(4711)));

            // Convert AST to string using toString()
            String exprString = originalExpr.toString();

            // Parse the string back into an AST
            SymbolicExpression parsedExpr = parser.parse(exprString, env);

            // Compare original AST with parsed AST using equals()
            assertEquals(originalExpr, parsedExpr, "Parser failed to reconstruct the correct AST.");
        }

    }

    @Test
    public void testComplexExpressionEvaluation() {
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor(env);

        // Create a complex expression: (2 * 3) + 4
        SymbolicExpression complexExpression = new Addition(
                new Multiplication(new Constant(2), new Constant(3)),
                new Constant(4));

        // Expected result
        SymbolicExpression expected = new Constant(10);

        // Evaluate the expression
        SymbolicExpression result = evaluator.evaluate(complexExpression, env);

        // Assert that the result matches the expected value
        assertEquals(expected, result);
    }

    @Test
    public void testScopes() throws IOException {
        CalculatorParser parser = new CalculatorParser();
        EvaluationVisitor evaluator;

        // Original test case
        Environment env1 = new Environment();
        evaluator = new EvaluationVisitor(env1);
        SymbolicExpression e = new Multiplication(new Scope(new Assignment(new Constant(2), new Variable("o"))),
                new Scope(new Assignment(new Constant(6), new Variable("p"))));
        assertEquals(evaluator.evaluate(e, env1), new Constant(12));

        // {1 = x} + {1 = x} -> 2.0
        Environment env2 = new Environment();
        evaluator = new EvaluationVisitor(env2);
        SymbolicExpression scope1 = parser.parse("{1 = x} + {1 = x}", env2);
        SymbolicExpression expected1 = new Constant(2);
        SymbolicExpression result1 = evaluator.evaluate(scope1, env2);
        assertEquals(expected1, result1);

        // {1 = x} + {x} -> 1.0 + x
        Environment env3 = new Environment();
        evaluator = new EvaluationVisitor(env3);
        SymbolicExpression scope2 = parser.parse("{1 = x} + {x}", env3);
        SymbolicExpression expected2 = new Addition(new Constant(1), new Variable("x"));
        SymbolicExpression result2 = evaluator.evaluate(scope2, env3);
        assertEquals(expected2, result2);

        // {{1 = x} = x} -> 1.0
        Environment env4 = new Environment();
        evaluator = new EvaluationVisitor(env4);
        SymbolicExpression scope3 = parser.parse("{{1 = x} = x}", env4);
        SymbolicExpression expected3 = new Constant(1);
        SymbolicExpression result3 = evaluator.evaluate(scope3, env4);
        assertEquals(expected3, result3);

        // {(2 = x) + {1 = x}} -> 3.0
        Environment env5 = new Environment();
        evaluator = new EvaluationVisitor(env5);
        SymbolicExpression scope4 = parser.parse("{(2 = x) + {1 = x}}", env5);
        SymbolicExpression expected4 = new Constant(3);
        SymbolicExpression result4 = evaluator.evaluate(scope4, env5);
        assertEquals(expected4, result4);

        // (1 = x) + {(2 + x = x) + {3 + x = x}} -> 10.0
        Environment env6 = new Environment();
        evaluator = new EvaluationVisitor(env6);
        SymbolicExpression scope5 = parser.parse("(1 = x) + {(2 + x = x) + {3 + x = x}}", env6);
        SymbolicExpression expected5 = new Constant(10);
        SymbolicExpression result5 = evaluator.evaluate(scope5, env6);
        assertEquals(expected5, result5);
    }

    @Test
    public void testInvalidExpressionParsing() {
        Environment env = new Environment();
        CalculatorParser parser = new CalculatorParser();

        // Test: Invalid variable name
        assertThrows(SyntaxErrorException.class, () -> parser.parse("1x = 5", env));

        // Test: Invalid expression
        assertThrows(SyntaxErrorException.class, () -> parser.parse("5 + ", env));
    }

    @Test
    public void testFunctionExpressions() throws IOException {
        System.out.println("Testing function expressions:");
        Environment env = new Environment();
        CalculatorParser parser = new CalculatorParser();
        EvaluationVisitor evaluator = new EvaluationVisitor(env);

        // max(x, y) function definition and evaluation
        SymbolicExpression maxDecl = parser.parse("function max(x, y)", env);
        assertTrue(maxDecl instanceof FunctionDeclaration);
        FunctionDeclaration maxFunc = (FunctionDeclaration) maxDecl;
        SymbolicExpression maxBody = parser.parse("if x < y { y } else { x }", env);
        assertTrue(maxBody instanceof Conditional);
        SymbolicExpression end = parser.parse("end", env);
        assertTrue(end instanceof End);
        ArrayList<SymbolicExpression> maxBodyList = new ArrayList<>();
        maxBodyList.add(maxBody);
        maxFunc.setFunctionBody(new Sequence(maxBodyList));
        env.put(new Variable("max"), maxFunc);

        // max(5, 7) -> 7.0
        SymbolicExpression maxCall = parser.parse("max(5, 7)", env);
        assertTrue(maxCall instanceof FunctionCall);
        SymbolicExpression maxExpected = new Constant(7);
        SymbolicExpression maxResult = evaluator.evaluate(maxCall, env);
        testEvaluating(maxExpected, maxCall, env, evaluator);
        System.out.println("max(5, 7) evaluated to: " + maxResult);
        assertEquals(maxExpected, maxResult);

        // max(5) -> too few arguments
        assertThrows(RuntimeException.class, () -> {
            SymbolicExpression tooFewArgs = parser.parse("max(5)", env);
            evaluator.evaluate(tooFewArgs, env);
        }, "Expected error for too few arguments in max(5)");

        // max(5, 7, 9) -> too many arguments
        assertThrows(RuntimeException.class, () -> {
            SymbolicExpression tooManyArgs = parser.parse("max(5, 7, 9)", env);
            evaluator.evaluate(tooManyArgs, env);
        }, "Expected error for too many arguments in max(5, 7, 9)");

        // gcd(a, b) function definition and evaluation
        SymbolicExpression gcdDecl = parser.parse("function gcd(a, b)", env);
        assertTrue(gcdDecl instanceof FunctionDeclaration);
        FunctionDeclaration gcdFunc = (FunctionDeclaration) gcdDecl;
        SymbolicExpression gcdAssignBa = parser.parse("b - a = ba", env);
        SymbolicExpression gcdAssignAb = parser.parse("a - b = ab", env);
        SymbolicExpression gcdBody = parser
                .parse("if a == b { a } else { if a < b { gcd(a, ba) } else { gcd(ab, b) } }", env);
        assertTrue(gcdBody instanceof Conditional);
        SymbolicExpression gcdEnd = parser.parse("end", env);
        assertTrue(gcdEnd instanceof End);
        ArrayList<SymbolicExpression> gcdBodyList = new ArrayList<>();
        gcdBodyList.add(gcdAssignBa);
        gcdBodyList.add(gcdAssignAb);
        gcdBodyList.add(gcdBody);
        gcdFunc.setFunctionBody(new Sequence(gcdBodyList));
        env.put(new Variable("gcd"), gcdFunc);

        // gcd(18, 30) -> 6.0
        SymbolicExpression gcdCall = parser.parse("gcd(18, 30)", env);
        assertTrue(gcdCall instanceof FunctionCall);
        SymbolicExpression gcdExpected = new Constant(6);
        SymbolicExpression gcdResult = evaluator.evaluate(gcdCall, env);
        testEvaluating(gcdExpected, gcdCall, env, evaluator);
        System.out.println("gcd(18, 30) evaluated to: " + gcdResult);
        assertEquals(gcdExpected, gcdResult);

        // gcd(8, 24) -> 8.0
        SymbolicExpression gcdCall2 = parser.parse("gcd(8, 24)", env);
        assertTrue(gcdCall2 instanceof FunctionCall);
        SymbolicExpression gcdExpected2 = new Constant(8);
        SymbolicExpression gcdResult2 = evaluator.evaluate(gcdCall2, env);
        testEvaluating(gcdExpected2, gcdCall2, env, evaluator);
        System.out.println("gcd(8, 24) evaluated to: " + gcdResult2);
        assertEquals(gcdExpected2, gcdResult2);
    }

    @Test
    public void testRecursiveFunction() throws IOException {
        Environment env = new Environment();
        CalculatorParser parser = new CalculatorParser();
        EvaluationVisitor evaluator = new EvaluationVisitor(env);

        SymbolicExpression funcDecl = parser.parse("function factorial(n)", env);
        assertTrue(funcDecl instanceof FunctionDeclaration);
        FunctionDeclaration factorial = (FunctionDeclaration) funcDecl;

        SymbolicExpression assignM = parser.parse("n - 1 = m", env);
        assertTrue(assignM instanceof Assignment);

        SymbolicExpression recursiveBody = parser.parse("if n > 1 { factorial(m) * n } else { 1 }", env);
        assertTrue(recursiveBody instanceof Conditional);

        SymbolicExpression end = parser.parse("end", env);
        assertTrue(end instanceof End);

        ArrayList<SymbolicExpression> body = new ArrayList<>();
        body.add(assignM);
        body.add(recursiveBody);
        factorial.setFunctionBody(new Sequence(body));

        env.put(new Variable("factorial"), factorial);

        SymbolicExpression factorialCall = parser.parse("factorial(5)", env);
        assertTrue(factorialCall instanceof FunctionCall);

        SymbolicExpression expected = new Constant(120);
        SymbolicExpression result = evaluator.evaluate(factorialCall, env);

        testEvaluating(expected, factorialCall, env, evaluator);
        assertEquals(expected, result, "Factorial(5) should evaluate to 120.0");
    }

    @Test
    public void testConditionalExpression() throws IOException {
        Environment env = new Environment();
        CalculatorParser parser = new CalculatorParser();
        EvaluationVisitor evaluator = new EvaluationVisitor(env);

        SymbolicExpression conditionalExpr = parser.parse("if 5 > 3 { 10 } else { 2 }", env);
        assertTrue(conditionalExpr instanceof Conditional);
        SymbolicExpression expected = new Constant(10);
        SymbolicExpression result = evaluator.evaluate(conditionalExpr, env);
        testEvaluating(expected, conditionalExpr, env, evaluator);
        System.out.println("if 5 > 3 { 10 } else { 2 } evaluated to: " + result);
        assertEquals(expected, result);

        SymbolicExpression falseConditionalExpr = parser.parse("if 3 == 5 { 10 } else { 2 }", env);
        assertTrue(falseConditionalExpr instanceof Conditional);
        SymbolicExpression falseExpected = new Constant(2);
        SymbolicExpression falseResult = evaluator.evaluate(falseConditionalExpr, env);
        testEvaluating(falseExpected, falseConditionalExpr, env, evaluator);
        assertEquals(falseExpected, falseResult);
    }

}
