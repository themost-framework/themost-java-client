package org.most.client;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URISyntaxException;
import java.util.HashMap;

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
public class ClientDataQueryable {

    private ClientDataService service_;
    private String model_;
    private ClientDataServiceParams params_;

    private ComparisonExpression expr_ = null;

    private LogicalOperator lop_ = LogicalOperator.and;

    private void append_() {

        if (this.expr_ == null) {
            return;
        }
        if (this.params_.$filter == null) {
            this.params_.$filter = this.expr_.toString();
        }
        else {
            this.params_.$filter = "(" + this.params_.$filter + ")" +
                    " " + this.lop_.toString() + " " +
                    "(" + this.expr_.toString() + ")";
        }
        this.expr_ = null;
    }

    public ClientDataQueryable(ClientDataService service, String model) {
        this.service_ = service;
        this.model_ = model;
        this.params_ = new ClientDataServiceParams();
    }

    public DataObjectArray take(Integer n) throws URISyntaxException, IOException {
        this.params_.$top = n;
        HashMap<String, Object> params = this.params_.toHashMap();
        params.put("$inlinecount","true");
        return (DataObjectArray)this.service_.get("/" + this.model_ + "/index.json", params);
    }

    public DataObject first() throws URISyntaxException, IOException {
        this.params_.$top = 1;
        this.params_.$skip = 0;
        DataObjectArray result = (DataObjectArray)this.service_.get("/" + this.model_ + "/index.json", this.params_.toHashMap());
        if ((result != null) && (result.size()>0)) {
            return result.get(0);
        }
        return null;
    }

    public Object value() throws URISyntaxException, IOException {
        this.params_.$top = 1;
        this.params_.$skip = 0;
        DataObjectArray result = (DataObjectArray)this.service_.get("/" + this.model_ + "/index.json", this.params_.toHashMap());
        if ((result != null) && (result.size()>0)) {
            DataObject result0 = result.get(0);
            if (result0.values().isEmpty()) {
                return null;
            }
            return result0.values().iterator().next();
        }
        return null;
    }

    public ClientDataQueryable skip(Integer n) {
        this.params_.$skip = n;
        return this;
    }

    /**
     * Prepares a custom filter
     * @param s - A string that represents a filter expression
     * @return
     */
    public ClientDataQueryable filter(String s) {
        this.params_.$filter = s;
        return this;
    }

    /**
     * Prepares a comparison expression
     * @param name - A string that represents the name of the field
     *             that is going to be used as left operand of this expression
     * @return
     */
    public ClientDataQueryable where(String name) {
        this.expr_ = new ComparisonExpression();
        this.expr_.left = new FieldExpression(name);
        return this;
    }

    public ClientDataQueryable getDay() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.getDay();
        }
        return this;
    }

    public ClientDataQueryable getMonth() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.getMonth();
        }
        return this;
    }

    public ClientDataQueryable getYear() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.getYear();
        }
        return this;
    }

    public ClientDataQueryable getDate() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.getDate();
        }
        return this;
    }

    public ClientDataQueryable getMinutes() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.getMinutes();
        }
        return this;
    }

    public ClientDataQueryable getSeconds() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.getSeconds();
        }
        return this;
    }

    public ClientDataQueryable getHours() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.getHours();
        }
        return this;
    }

    public ClientDataQueryable indexOf(String s) {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.indexOf(s);
        }
        return this;
    }

    public ClientDataQueryable substring(int start) {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.substring(start);
        }
        return this;
    }

    public ClientDataQueryable substring(int start, int length) {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.substring(start, length);
        }
        return this;
    }

    public ClientDataQueryable startsWith(String s) {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.startsWith(s);
        }
        return this;
    }

    public ClientDataQueryable endsWith(String s) {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.endsWith(s);
        }
        return this;
    }

    public ClientDataQueryable contains(String s) {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.contains(s);
        }
        return this;
    }

    public ClientDataQueryable trim() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.trim();
        }
        return this;
    }

    public ClientDataQueryable toLowerCase() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.toLowerCase();
        }
        return this;
    }

    public ClientDataQueryable toUpperCase() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.toUpperCase();
        }
        return this;
    }

    public ClientDataQueryable min() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.min();
        }
        return this;
    }

    public ClientDataQueryable max() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.max();
        }
        return this;
    }

    public ClientDataQueryable sum() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.sum();
        }
        return this;
    }

    public ClientDataQueryable count() {
        if ((this.expr_ != null) && (this.expr_.left != null)) {
            this.expr_.left.count();
        }
        return this;
    }

    public ClientDataQueryable and(String name) {
        this.expr_ = new ComparisonExpression();
        this.expr_.left = new FieldExpression(name);
        this.lop_ = LogicalOperator.and;
        return this;
    }

    public ClientDataQueryable or(String name) {
        this.expr_ = new ComparisonExpression();
        this.expr_.left = new FieldExpression(name);
        this.lop_ = LogicalOperator.or;
        return this;
    }

    private ClientDataQueryable comparison_(ComparisonOperator operator, Object value) throws InvalidObjectException {
        if (this.expr_ == null) {
            throw new InvalidObjectException("The left operand of current expression is missing or is not yet implemented.");
        }
        this.expr_.operator = operator;
        this.expr_.right = value;
        this.append_();
        return this;
    }

    public ClientDataQueryable equal(Object value) throws InvalidObjectException {
        return this.comparison_(ComparisonOperator.eq,value);
    }

    public ClientDataQueryable eq(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable notEqual(Object value) throws InvalidObjectException {
        return this.comparison_(ComparisonOperator.ne,value);
    }

    public ClientDataQueryable ne(Object value) throws InvalidObjectException {
        return this.comparison_(ComparisonOperator.ne,value);
    }

    public ClientDataQueryable greaterThan(Object value) throws InvalidObjectException {
        return this.comparison_(ComparisonOperator.gt,value);
    }

    public ClientDataQueryable gt(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable greaterOrEqual(Object value) throws InvalidObjectException {
        return this.comparison_(ComparisonOperator.ge,value);
    }

    public ClientDataQueryable ge(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable lowerThan(Object value) throws InvalidObjectException {
        return this.comparison_(ComparisonOperator.lt,value);
    }

    public ClientDataQueryable lt(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable lowerOrEqual(Object value) throws InvalidObjectException {
        return this.comparison_(ComparisonOperator.le,value);
    }

    public ClientDataQueryable le(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable groupBy(String name) {
        this.params_.$groupby.add(name);
        return this;
    }

    public ClientDataQueryable groupBy(String[] name) {
        int i = 0;
        while (i < name.length) {
            this.params_.$groupby.add(name[i]);
            i++;
        }
        return this;
    }

    public ClientDataQueryable orderBy(String name) {
        this.params_.$orderby.clear();
        this.params_.$orderby.add(name);
        return this;
    }

    public ClientDataQueryable orderBy(String[] name) {
        this.params_.$orderby.clear();
        int i = 0;
        while (i < name.length) {
            this.params_.$orderby.add(name[i]);
            i++;
        }
        return this;
    }

    public ClientDataQueryable thenBy(String name) {
        this.params_.$orderby.add(name);
        return this;
    }

    public ClientDataQueryable orderByDescending(String name) {
        this.params_.$orderby.clear();
        this.params_.$orderby.add(name.concat(" desc"));
        return this;
    }

    public ClientDataQueryable thenByDescending(String name) {
        this.params_.$orderby.add(name.concat(" desc"));
        return this;
    }

    /**
     * Initializes field selection and adds the specified field or array of fields
     * @param name - A param array of strings which are going to be added in field selection.
     * @return
     */
    public ClientDataQueryable select(String... name) {
        this.params_.$select.clear();
        int i = 0;
        while (i < name.length) {
            this.params_.$select.add(new FieldExpression(name[i]));
            i++;
        }
        return this;
    }

    /**
     * Sets an alias for the last field added in selection list.
     * @param alias - A string that represents an alias for a selected field e.g. givenName as name
     * @return
     */
    public ClientDataQueryable as(String alias) {
        if (this.params_.$select.size()>0) {
            FieldExpression expr = this.params_.$select.get(this.params_.$select.size()-1);
            expr.as(alias);
        }
        return this;
    }

    public ClientDataQueryable alsoSelect(String... name) {
        int i = 0;
        while (i < name.length) {
            this.params_.$select.add(new FieldExpression(name[i]));
            i++;
        }
        return this;
    }

    public ClientDataQueryable select(FieldExpression... expr) {
        this.params_.$select.clear();
        int i = 0;
        while (i < expr.length) {
            this.params_.$select.add(expr[i]);
            i++;
        }
        return this;
    }

    public ClientDataQueryable alsoSelect(FieldExpression... expr) {
        int i = 0;
        while (i < expr.length) {
            this.params_.$select.add(expr[i]);
            i++;
        }
        return this;
    }

    public DataObject save(DataObject data) throws URISyntaxException, IOException {
        Object result = this.service_.post("/" + this.model_ + "/edit.json", null, data);
        if (result instanceof DataObject) {
            return (DataObject)result;
        }
        else if (result instanceof DataObject[]) {
            if (((DataObject[])result).length>0) {
                return ((DataObject[])result)[0];
            }
        }
        return null;
    }

    public Object remove(DataObject data) throws URISyntaxException, IOException {
        Object result = this.service_.post("/" + this.model_ + "/remove.json", null, data);
        if (result instanceof DataObject) {
            return (DataObject)result;
        }
        else if (result instanceof DataObject[]) {
            if (((DataObject[])result).length>0) {
                return ((DataObject[])result)[0];
            }
        }
        return null;
    }


}
