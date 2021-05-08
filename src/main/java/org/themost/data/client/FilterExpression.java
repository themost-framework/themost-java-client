package org.themost.data.client;

public class FilterExpression {

    private String expr_;

    public FilterExpression(String s) {
        this.expr_ = s;
    }

    public String toString() {
        return this.expr_;
    }

}
