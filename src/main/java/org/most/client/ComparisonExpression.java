package org.most.client;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 Copyright (c) 2015, Kyriakos Barbounakis k.barbounakis@gmail.com
 Anthi Oikonomou anthioikonomou@gmail.com
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of MOST Web Framework nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
