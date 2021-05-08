package org.themost.data.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class ClientDataQueryable {

    private final ClientDataService _service;
    private final String _model;
    private final ClientDataServiceParams _params;

    private ComparisonExpression _expr = null;

    private LogicalOperator _lop = LogicalOperator.and;

    private void _append() {

        if (this._expr == null) {
            return;
        }
        if (this._params.$filter == null) {
            this._params.$filter = this._expr.toString();
        }
        else {
            this._params.$filter = "(" + this._params.$filter + ")" +
                    " " + this._lop.toString() + " " +
                    "(" + this._expr.toString() + ")";
        }
        this._expr = null;
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
        JsonNode node = (JsonNode)this._service.get(
                new DataServiceExecuteOptions() {{
                    method = "GET";
                    url = ClientDataQueryable.this._model;
                    query = queryParams;
                }}
        );
        if (node == null) {
            return null;
        }
        if (node.has("value")) {
            ArrayNode elements = (ArrayNode) node.get("value");
            return elements.get(0);
        }
        if (node instanceof ObjectNode) {
            return node;
        }
        throw new InvalidObjectException("Invalid response. Expected object.");
    }

    public <T> T getItem(Class<T> type) throws URISyntaxException, IOException {
        JsonNode item = (JsonNode)this.getItem();
        if (item == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper.treeToValue(item, type);
    }

    public Object getItems() throws URISyntaxException, IOException {
        HashMap<String, Object> queryParams = this._params.toHashMap();
        JsonNode node = (JsonNode)this._service.get(
                new DataServiceExecuteOptions() {{
                    method = "GET";
                    url = ClientDataQueryable.this._model;
                    query = queryParams;
                }}
        );
        if (node instanceof ArrayNode) {
            return node;
        }
        // format response
        if (node.has("value")) {
            Object elements = node.get("value");
            if (elements instanceof ArrayNode) {
                return elements;
            }
        }
        throw new InvalidObjectException("Invalid response. Expected an array.");
    }

    public <T> List<T> getItems(Class<T> type) throws URISyntaxException, IOException {
        ArrayNode node = (ArrayNode)this.getItems();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        ArrayList<T> result = new ArrayList<>();
        for (JsonNode jsonNode : node) {
            result.add(mapper.treeToValue(jsonNode, type));
        }
        return result;
    }

    public Object getList() throws URISyntaxException, IOException {
        this._params.$count = true;
        HashMap<String, Object> queryParams = this._params.toHashMap();
        JsonNode node = (JsonNode)this._service.get(
                new DataServiceExecuteOptions() {{
                    method = "GET";
                    url = ClientDataQueryable.this._model;
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
     * @return ClientDataQueryable
     */
    public ClientDataQueryable filter(String s) {
        this._params.$filter = s;
        return this;
    }

    /**
     * Prepares a comparison expression
     * @param name - A string that represents the name of the field
     *             that is going to be used as left operand of this expression
     * @return ClientDataQueryable
     */
    public ClientDataQueryable where(String name) {
        this._expr = new ComparisonExpression();
        this._expr.left = new FieldExpression(name);
        return this;
    }

    public ClientDataQueryable getDay() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.getDay();
        }
        return this;
    }

    public ClientDataQueryable getMonth() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.getMonth();
        }
        return this;
    }

    public ClientDataQueryable getYear() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.getYear();
        }
        return this;
    }

    public ClientDataQueryable getDate() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.getDate();
        }
        return this;
    }

    public ClientDataQueryable getMinutes() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.getMinutes();
        }
        return this;
    }

    public ClientDataQueryable getSeconds() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.getSeconds();
        }
        return this;
    }

    public ClientDataQueryable getHours() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.getHours();
        }
        return this;
    }

    public ClientDataQueryable indexOf(String s) {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.indexOf(s);
        }
        return this;
    }

    public ClientDataQueryable substring(int start) {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.substring(start);
        }
        return this;
    }

    public ClientDataQueryable substring(int start, int length) {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.substring(start, length);
        }
        return this;
    }

    public ClientDataQueryable startsWith(String s) {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.startsWith(s);
        }
        return this;
    }

    public ClientDataQueryable endsWith(String s) {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.endsWith(s);
        }
        return this;
    }

    public ClientDataQueryable contains(String s) {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.contains(s);
        }
        return this;
    }

    public ClientDataQueryable trim() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.trim();
        }
        return this;
    }

    public ClientDataQueryable toLowerCase() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.toLowerCase();
        }
        return this;
    }

    public ClientDataQueryable toUpperCase() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.toUpperCase();
        }
        return this;
    }

    public ClientDataQueryable min() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.min();
        }
        return this;
    }

    public ClientDataQueryable max() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.max();
        }
        return this;
    }

    public ClientDataQueryable sum() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.sum();
        }
        return this;
    }

    public ClientDataQueryable count() {
        if ((this._expr != null) && (this._expr.left != null)) {
            this._expr.left.count();
        }
        return this;
    }

    public ClientDataQueryable and(String name) {
        this._expr = new ComparisonExpression();
        this._expr.left = new FieldExpression(name);
        this._lop = LogicalOperator.and;
        return this;
    }

    public ClientDataQueryable or(String name) {
        this._expr = new ComparisonExpression();
        this._expr.left = new FieldExpression(name);
        this._lop = LogicalOperator.or;
        return this;
    }

    private ClientDataQueryable _compare(ComparisonOperator operator, Object value) throws InvalidObjectException {
        if (this._expr == null) {
            throw new InvalidObjectException("The left operand of current expression is missing or is not yet implemented.");
        }
        this._expr.operator = operator;
        this._expr.right = value;
        this._append();
        return this;
    }

    public ClientDataQueryable equal(Object value) throws InvalidObjectException {
        return this._compare(ComparisonOperator.eq,value);
    }

    public ClientDataQueryable eq(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable notEqual(Object value) throws InvalidObjectException {
        return this._compare(ComparisonOperator.ne,value);
    }

    public ClientDataQueryable ne(Object value) throws InvalidObjectException {
        return this._compare(ComparisonOperator.ne,value);
    }

    public ClientDataQueryable greaterThan(Object value) throws InvalidObjectException {
        return this._compare(ComparisonOperator.gt,value);
    }

    public ClientDataQueryable gt(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable greaterOrEqual(Object value) throws InvalidObjectException {
        return this._compare(ComparisonOperator.ge,value);
    }

    public ClientDataQueryable between(Object value1, Object value2) throws InvalidObjectException {
        String name = this._expr.left.name;
        ClientDataQueryable q =new ClientDataQueryable(this._service, this._model)
                .where(name).greaterOrEqual(value1)
                .and(name).lowerOrEqual(value2);
        if (this._params.$filter == null) {
            this._params.$filter = String.format("(%s)", q._params.$filter);
        }
        else {
            this._params.$filter = String.format("(%s) ",this._params.$filter) +
                    this._lop.toString() +
                    String.format(" (%s)", q._params.$filter);
        }
        this._expr = null;
        return this;
    }

    public ClientDataQueryable ge(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable lowerThan(Object value) throws InvalidObjectException {
        return this._compare(ComparisonOperator.lt,value);
    }

    public ClientDataQueryable lt(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable lowerOrEqual(Object value) throws InvalidObjectException {
        return this._compare(ComparisonOperator.le,value);
    }

    public ClientDataQueryable le(Object value) throws InvalidObjectException {
        return this.equal(value);
    }

    public ClientDataQueryable groupBy(String ... name) {
        this._params.$groupby.clear();
        int i = 0;
        while (i < name.length) {
            this._params.$groupby.add(new FieldExpression(name[i]));
            i++;
        }
        return this;
    }

    public ClientDataQueryable orderBy(String name) {
        this._params.$orderby.clear();
        this._params.$orderby.add(name);
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

    public ClientDataQueryable expand(String ... name) {
        // clear
        this._params.$expand.clear();
        // add items
        this._params.$expand.addAll(Arrays.asList(name));
        return this;
    }

    /**
     * Sets an alias for the last field added in selection list.
     * @param alias - A string that represents an alias for a selected field e.g. givenName as name
     * @return ClientDataQueryable
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

    public Object remove() throws IOException, URISyntaxException {
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
