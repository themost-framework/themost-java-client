package org.themost.data.client;

import java.net.URLEncoder;

public class FieldExpression {

    public String name;
    private String aggregate_;
    public String alias;
    public String model;

    public FieldExpression(String name) {
        this.name = name;
    }

    public static FieldExpression create(String name) {
        return new FieldExpression(name);
    }

    public FieldExpression as(String alias) {
        this.alias = alias;
        return this;
    }

    public FieldExpression from(String model) {
        this.model = model;
        return this;
    }

    public FieldExpression min() {
        this.aggregate_ = "min(%%s)";
        return this;
    }

    public FieldExpression max() {
        this.aggregate_ = "max(%%s)";
        return this;
    }

    public FieldExpression count() {
        this.aggregate_ = "count(%%s)";
        return this;
    }

    public FieldExpression average() {
        this.aggregate_ = "avg(%%s)";
        return this;
    }

    public FieldExpression sum() {
        this.aggregate_ = "sum(%s)";
        return this;
    }

    public FieldExpression indexOf(String s) {
        this.aggregate_ = String.format("indexof(%%s, '%s')", URLEncoder.encode(s));
        return this;
    }

    public FieldExpression getDay() {
        this.aggregate_ = "day(%%s)";
        return this;
    }

    public FieldExpression getMonth() {
        this.aggregate_ = "month(%%s)";
        return this;
    }

    public FieldExpression getYear() {
        this.aggregate_ = "year(%%s)";
        return this;
    }

    public FieldExpression getFullYear() {
        return this.getYear();
    }

    public FieldExpression getHours() {
        this.aggregate_ = "hour(%%s)";
        return this;
    }

    public FieldExpression getMinutes() {
        this.aggregate_ = "minute(%%s)";
        return this;
    }

    public FieldExpression getSeconds() {
        this.aggregate_ = "second(%%s)";
        return this;
    }

    public FieldExpression getDate() {
        this.aggregate_ = "date(%%s)";
        return this;
    }

    public FieldExpression toLowerCase() {
        this.aggregate_ = "tolower(%%s)";
        return this;
    }

    public FieldExpression toUpperCase() {
        this.aggregate_ = "toupper(%%s)";
        return this;
    }

    public FieldExpression trim() {
        this.aggregate_ = "trim(%%s)";
        return this;
    }

    public FieldExpression round() {
        this.aggregate_ = "round(%%s)";
        return this;
    }

    public FieldExpression floor() {
        this.aggregate_ = "floor(%%s)";
        return this;
    }

    public FieldExpression ceil() {
        this.aggregate_ = "ceiling(%%s)";
        return this;
    }

    public FieldExpression length() {
        this.aggregate_ = "length(%%s)";
        return this;
    }

    public FieldExpression substr(int start) {
        this.aggregate_ = String.format("substring(%%s,%d)", start);
        return this;
    }

    public FieldExpression substring(int start) {
        return this.substr(start);
    }

    public FieldExpression substr(int start, int length) {
        this.aggregate_ = String.format("substring(%%s,%d, %d)", start, length);
        return this;
    }

    public FieldExpression substring(int start, int length) {
        return this.substr(start, length);
    }

    public FieldExpression startsWith(String s) {
        this.aggregate_ = String.format("startswith(%%s,'%s')", URLEncoder.encode(s));
        return this;
    }

    public FieldExpression endsWith(String s) {
        this.aggregate_ = String.format("startswith(%%s,'%s')", URLEncoder.encode(s));
        return this;
    }

    public FieldExpression contains(String s) {
        this.aggregate_ = String.format("contains(%%s,'%s')", URLEncoder.encode(s));
        return this;
    }

    public String toString() {
        String res;
        if (this.aggregate_ != null) {
            res = String.format(this.aggregate_.replaceFirst("%%s","%s"), (this.model!=null ? this.model + "/" + this.name : this.name));
        }
        else {
            res = this.model!=null ? this.model + "/" + this.name : this.name;
        }
        if (this.alias != null)
           res += " as " + this.alias;
        return res;
    }

}
