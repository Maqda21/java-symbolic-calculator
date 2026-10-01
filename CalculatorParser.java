package org.ioopm.calculator.parser;

import org.ioopm.calculator.ast.*;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;
import java.util.*;

public class CalculatorParser {
    private StreamTokenizer st;
    private Environment vars;
    private static char MULTIPLY = '*';
    private static char ADDITION = '+';
    private static char SUBTRACTION = '-';
    private static char DIVISION = '/';
    private static char ASSIGNMENT = '=';

    private final ArrayList<String> unallowedVars = new ArrayList<>(
            Arrays.asList("Quit", "Vars", "Clear", "if", "else", "function", "end"));

    public SymbolicExpression parse(String inputString, Environment vars) throws IOException {
        this.st = new StreamTokenizer(new StringReader(inputString));
        this.vars = vars;
        this.st.ordinaryChar('-');
        this.st.ordinaryChar('/');
        this.st.eolIsSignificant(true);
        this.st.nextToken();
        if (this.st.ttype == this.st.TT_EOF) {
            throw new SyntaxErrorException("Error: Empty input");
        }
        SymbolicExpression result = expression();
        if (this.st.ttype != this.st.TT_EOF) {
            throw new SyntaxErrorException("Error: Extra tokens after expression: " + (char) this.st.ttype);
        }
        return result;
    }

    private SymbolicExpression expression() throws IOException {
        SymbolicExpression result = term();
        while (this.st.ttype == ADDITION || this.st.ttype == SUBTRACTION) {
            char operator = (char) this.st.ttype;
            this.st.nextToken();
            SymbolicExpression rhs = term();
            if (operator == ADDITION) {
                result = new Addition(result, rhs);
            } else {
                result = new Subtraction(result, rhs);
            }
        }
        if (this.st.ttype == ASSIGNMENT) {
            this.st.nextToken();
            SymbolicExpression rhs = expression();
            result = new Assignment(result, rhs);
        }
        return result;
    }

    private SymbolicExpression term() throws IOException {
        SymbolicExpression result = factor();
        while (this.st.ttype == MULTIPLY || this.st.ttype == DIVISION) {
            char operator = (char) this.st.ttype;
            this.st.nextToken();
            SymbolicExpression rhs = factor();
            if (operator == MULTIPLY) {
                result = new Multiplication(result, rhs);
            } else {
                result = new Division(result, rhs);
            }
        }
        return result;
    }

    private SymbolicExpression factor() throws IOException {
        if (this.st.ttype == this.st.TT_EOF) {
            throw new SyntaxErrorException("Error: Unexpected end of input");
        }

        SymbolicExpression result;
        if (this.st.ttype == '{') {
            this.st.nextToken();
            result = expression();
            if (this.st.ttype != '}') {
                throw new SyntaxErrorException("Error: Expected '}' after scope, got " + (char) this.st.ttype);
            }
            this.st.nextToken();
            result = new Scope(result);
        } else if (this.st.ttype == '(') {
            this.st.nextToken();
            result = expression();
            if (this.st.ttype != ')') {
                throw new SyntaxErrorException("Error: Expected ')' after expression, got " + (char) this.st.ttype);
            }
            this.st.nextToken();
        } else if (this.st.ttype == '-') {
            this.st.nextToken();
            result = new Negation(factor());
        } else if (this.st.ttype == this.st.TT_WORD) {
            String word = this.st.sval;
            this.st.nextToken();
            if (word.equals("Sin")) {
                result = new Sin(factor());
            } else if (word.equals("Cos")) {
                result = new Cos(factor());
            } else if (word.equals("Exp")) {
                result = new Exp(factor());
            } else if (word.equals("Neg")) {
                result = new Negation(factor());
            } else if (word.equals("Log")) {
                result = new Log(factor());
            } else if (word.equals("Quit")) {
                result = Quit.instance();
            } else if (word.equals("Vars")) {
                result = Vars.instance();
            } else if (word.equals("Clear")) {
                result = Clear.instance();
            } else if (word.equals("if")) {
                result = parseConditional();
            } else if (word.equals("function")) {
                result = parseFunctionDeclaration();
            } else if (word.equals("end")) {
                result = End.instance();
            } else {
                if (this.st.ttype == '(') {
                    this.st.nextToken();
                    ArrayList<SymbolicExpression> args = parseArguments();
                    if (this.st.ttype != ')') {
                        throw new SyntaxErrorException(
                                "Error: Expected ')' after arguments, got " + (char) this.st.ttype);
                    }
                    this.st.nextToken();
                    result = new FunctionCall(word, args);
                } else {
                    result = new Variable(word);
                }
            }
        } else if (this.st.ttype == this.st.TT_NUMBER) {
            result = new Constant(this.st.nval);
            this.st.nextToken();
        } else {
            throw new SyntaxErrorException("Error: Unexpected token: " + (char) this.st.ttype);
        }
        return result;
    }

    private SymbolicExpression parseConditional() throws IOException {
        SymbolicExpression lhs = factor();
        String operation;
        if (this.st.ttype == '<') {
            operation = "<";
            this.st.nextToken();
        } else if (this.st.ttype == '>') {
            operation = ">";
            this.st.nextToken();
        } else if (this.st.ttype == '=') {
            this.st.nextToken();
            if (this.st.ttype == '=') {
                operation = "==";
                this.st.nextToken();
            } else {
                throw new SyntaxErrorException("Error: Expected '==' for equality, got " + (char) this.st.ttype);
            }
        } else {
            throw new SyntaxErrorException(
                    "Error: Expected comparison operator (<, >, ==), got " + (char) this.st.ttype);
        }
        SymbolicExpression rhs = factor();

        if (this.st.ttype != '{') {
            throw new SyntaxErrorException("Error: Expected '{' after condition, got " + (char) this.st.ttype);
        }
        this.st.nextToken();
        SymbolicExpression ifExpr = expression();
        if (this.st.ttype != '}') {
            throw new SyntaxErrorException("Error: Expected '}' after if expression, got " + (char) this.st.ttype);
        }
        this.st.nextToken();

        if (this.st.ttype != this.st.TT_WORD || !this.st.sval.equals("else")) {
            throw new SyntaxErrorException("Error: Expected 'else', got "
                    + (this.st.ttype == this.st.TT_WORD ? this.st.sval : (char) this.st.ttype));
        }
        this.st.nextToken();

        if (this.st.ttype != '{') {
            throw new SyntaxErrorException("Error: Expected '{' after else, got " + (char) this.st.ttype);
        }
        this.st.nextToken();
        SymbolicExpression elseExpr = expression();
        if (this.st.ttype != '}') {
            throw new SyntaxErrorException("Error: Expected '}' after else expression, got " + (char) this.st.ttype);
        }
        this.st.nextToken();

        return new Conditional(operation, lhs, rhs, ifExpr, elseExpr);
    }

    private SymbolicExpression parseFunctionDeclaration() throws IOException {
        if (this.st.ttype != this.st.TT_WORD) {
            throw new SyntaxErrorException(
                    "Error: Expected function name after 'function', got " + (char) this.st.ttype);
        }
        String functionName = this.st.sval;
        this.st.nextToken();

        if (this.st.ttype != '(') {
            throw new SyntaxErrorException("Error: Expected '(' after function name, got " + (char) this.st.ttype);
        }
        this.st.nextToken();

        ArrayList<Variable> params = parseParameters();

        if (this.st.ttype != ')') {
            throw new SyntaxErrorException("Error: Expected ')' after parameters, got " + (char) this.st.ttype);
        }
        this.st.nextToken();

        return new FunctionDeclaration(functionName, params);
    }

    private ArrayList<Variable> parseParameters() throws IOException {
        ArrayList<Variable> params = new ArrayList<>();
        if (this.st.ttype == ')')
            return params; // Empty parameter list
        while (true) {
            if (this.st.ttype != this.st.TT_WORD) {
                throw new SyntaxErrorException("Error: Expected parameter name, got " + (char) this.st.ttype);
            }
            params.add(new Variable(this.st.sval));
            this.st.nextToken();
            if (this.st.ttype == ')')
                break;
            if (this.st.ttype != ',') {
                throw new SyntaxErrorException(
                        "Error: Expected ',' or ')' in parameter list, got " + (char) this.st.ttype);
            }
            this.st.nextToken();
        }
        return params;
    }

    private SymbolicExpression parseFunctionCall() throws IOException {
        if (this.st.ttype != this.st.TT_WORD) {
            throw new SyntaxErrorException("Error: Expected function name, got " + (char) this.st.ttype);
        }
        String functionName = this.st.sval;
        this.st.nextToken();

        if (this.st.ttype != '(') {
            // No parentheses, treat as a variable (per instruction)
            return new Variable(functionName);
        }
        this.st.nextToken(); // Consume '('

        ArrayList<SymbolicExpression> args = parseArguments();

        if (this.st.ttype != ')') {
            throw new SyntaxErrorException("Error: Expected ')' after arguments, got " + (char) this.st.ttype);
        }
        this.st.nextToken(); // Consume ')'

        return new FunctionCall(functionName, args);
    }

    private ArrayList<SymbolicExpression> parseArguments() throws IOException {
        ArrayList<SymbolicExpression> args = new ArrayList<>();
        if (this.st.ttype == ')')
            return args; // Empty argument list, e.g., f()

        while (true) {
            if (this.st.ttype == this.st.TT_NUMBER) {
                args.add(new Constant(this.st.nval));
                this.st.nextToken();
            } else if (this.st.ttype == this.st.TT_WORD) {
                args.add(new Variable(this.st.sval));
                this.st.nextToken();
            } else {
                throw new SyntaxErrorException(
                        "Error: Expected number or identifier as argument, got " + (char) this.st.ttype);
            }
            if (this.st.ttype == ')')
                break;
            if (this.st.ttype != ',') {
                throw new SyntaxErrorException(
                        "Error: Expected ',' or ')' in argument list, got " + (char) this.st.ttype);
            }
            this.st.nextToken();
        }
        return args;
    }
}