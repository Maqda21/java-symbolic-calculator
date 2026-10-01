package org.ioopm.calculator.parser;

/**
 * Exception thrown when attempting to reassign a named constant.
 */
public class IllegalAssignmentException extends RuntimeException {
    
    public IllegalAssignmentException(String message) {
        super(message);
    }
}
