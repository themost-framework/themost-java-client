package org.themost.data.client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComparisonExpression {

    public FieldExpression left;
    public Object right;
    public ComparisonOperator operator = ComparisonOperator.eq;

    public String toString() {
        String s = "null";
        if (this.right != null) {
            s = ComparisonExpression.escape(this.right);
        }
        return this.left.toString() + " " + this.operator.toString() + " " + s;
    }

    private static SimpleDateFormat ISODateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");

    public static String escape(Object value) {
        //0. null
        if (value == null)
            return "null";
        //1. datetime
        else if (value instanceof Date) {
            return ISODateFormatter.format((Date)value);
        }
        //2. boolean
        else if (value instanceof Boolean) {
            return (Boolean)value ? "true" : "false";
        }
        //3. integer
        else if ((value instanceof Integer) || Integer.class.isAssignableFrom(value.getClass())) {
            return String.format("%d", (int)value);
        }
        //4. numeric
        else if ((value instanceof Number) || Number.class.isAssignableFrom(value.getClass())) {
            return String.format("%f", (float)value);
        }
        //5. string
        else if (value instanceof String) {
            return String.format("'%s'", (String)value);
        }
        //6. filter expression
        else if (value instanceof FilterExpression) {
            return ((FilterExpression)value).toString();
        }
        //7. other
        else {
            return String.format("'%s'", value.toString());
        }
    }

}
