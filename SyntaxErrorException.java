package org.ioopm.calculator.parser;

/**
 * Exception thrown when a syntax error is encountered in the input.
 */
public class SyntaxErrorException extends RuntimeException {
    
    public SyntaxErrorException(String message) {
        super(message);
    }
}
