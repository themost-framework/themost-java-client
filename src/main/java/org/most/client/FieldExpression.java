package org.most.client;

import java.net.URLEncoder;

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
