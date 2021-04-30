package org.themost.data.client;

public class InvalidExpression extends Exception {
    public InvalidExpression() {
        super("Expression is invalid");
    }
    public InvalidExpression(String message) {
        super(message);
    }
}
