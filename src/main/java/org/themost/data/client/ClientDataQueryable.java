package org.themost.data.client;

import com.fasterxml.jackson.databind.JsonNode;
import net.sf.json.util.JSONTokener;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 *
 */
public class ClientDataQueryable {

    private final ClientDataService _service;
    private final String _model;
    private final ClientDataServiceParams _params;

    private ComparisonExpression expr_ = null;

    private LogicalOperator lop_ = LogicalOperator.and;

    private void append_() {

        if (this.expr_ == null) {
            return;
        }
        if (this._params.$filter == null) {
            this._params.$filter = this.expr_.toString();
        }
        else {
            this._params.$filter = "(" + this._params.$filter + ")" +
                    " " + this.lop_.toString() + " " +
                    "(" + this.expr_.toString() + ")";
        }
        this.expr_ = null;
    }

    public ClientDataQueryable(ClientDataService service, String model) {
        this._service = service;
        this._model = model;
        this._params = new ClientDataServiceParams();
    }

    public ClientDataQueryable take(Integer n) {
        this._params.$top = n;
        return this;
    }

    public Object getItem() throws URISyntaxException, IOException {
        this._params.$top = 1;
        this._params.$skip = 0;
        HashMap<String, Object> queryParams = this._params.toHashMap();
        String relativeUrl = this._model;
        JsonNode node = (JsonNode)this._service.get(
                new DataServiceExecuteOptions() {{
                    method = "GET";
                    url = relativeUrl;
                    query = queryParams;
                }}
        );
        if (node == null) {
            return null;
        }
        return node.get(0);
    }

    public Object getItems() throws URISyntaxException, IOException {
        HashMap<String, Object> queryParams = this._params.toHashMap();
        String relativeUrl = this._model;
        JsonNode node = (JsonNode)this._service.get(
                new DataServiceExecuteOptions() {{
                    method = "GET";
                    url = relativeUrl;
                    query = queryParams;
                }}
        );
        if (node == null) {
            return new Object[0];
        }
        return node;
    }

    public ClientDataQueryable skip(Integer n) {
        this._params.$skip = n;
        return this;
    }

    /**
     * Prepares a custom filter
     * @param s - A string that represents a filter expression
     * @return
     */
    public ClientDataQueryable filter(String s) {
        this._params.$filter = s;
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
        this._params.$groupby.add(name);
        return this;
    }

    public ClientDataQueryable groupBy(String[] name) {
        int i = 0;
        while (i < name.length) {
            this._params.$groupby.add(name[i]);
            i++;
        }
        return this;
    }

    public ClientDataQueryable orderBy(String name) {
        this._params.$orderby.clear();
        this._params.$orderby.add(name);
        return this;
    }

    public ClientDataQueryable orderBy(String[] name) {
        this._params.$orderby.clear();
        int i = 0;
        while (i < name.length) {
            this._params.$orderby.add(name[i]);
            i++;
        }
        return this;
    }

    public ClientDataQueryable thenBy(String name) {
        this._params.$orderby.add(name);
        return this;
    }

    public ClientDataQueryable orderByDescending(String name) {
        this._params.$orderby.clear();
        this._params.$orderby.add(name.concat(" desc"));
        return this;
    }

    public ClientDataQueryable thenByDescending(String name) {
        this._params.$orderby.add(name.concat(" desc"));
        return this;
    }

    /**
     * Initializes field selection and adds the specified field or array of fields
     * @param name - A param array of strings which are going to be added in field selection.
     * @return ClientDataQueryable
     */
    public ClientDataQueryable select(String... name) {
        this._params.$select.clear();
        int i = 0;
        while (i < name.length) {
            this._params.$select.add(new FieldExpression(name[i]));
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
        if (this._params.$select.size()>0) {
            FieldExpression expr = this._params.$select.get(this._params.$select.size()-1);
            expr.as(alias);
        }
        return this;
    }

    public ClientDataQueryable alsoSelect(String... name) {
        int i = 0;
        while (i < name.length) {
            this._params.$select.add(new FieldExpression(name[i]));
            i++;
        }
        return this;
    }

    public ClientDataQueryable select(FieldExpression... expr) {
        this._params.$select.clear();
        int i = 0;
        while (i < expr.length) {
            this._params.$select.add(expr[i]);
            i++;
        }
        return this;
    }

    public ClientDataQueryable alsoSelect(FieldExpression... expr) {
        int i = 0;
        while (i < expr.length) {
            this._params.$select.add(expr[i]);
            i++;
        }
        return this;
    }

    public Object save(Object any) throws URISyntaxException, IOException {
        JsonNode node;
        node = (JsonNode)this._service.post(
                new DataServiceExecuteOptions() {{
                    url = ClientDataQueryable.this._model;
                    data = any;
                }}
        );
        return node;
    }

    public Object remove() throws URISyntaxException, IOException {
        JsonNode node;
        node = (JsonNode) this._service.execute(
                new DataServiceExecuteOptions() {{
                    method = "DELETE";
                    url = ClientDataQueryable.this._model;
                }}
        );
        return node;
    }


}
