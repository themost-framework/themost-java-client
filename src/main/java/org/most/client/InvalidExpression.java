package org.most.client;

/**
 * Created by kbarbounakis on 12/21/15.
 */
public class InvalidExpression extends Exception {
    public InvalidExpression() {
        super("Expression is invalid");
    }
    public InvalidExpression(String message) {
        super(message);
    }
}
