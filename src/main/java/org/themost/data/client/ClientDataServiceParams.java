package org.themost.data.client;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientDataServiceParams {

    public final List<FieldExpression> $select = new ArrayList<FieldExpression>();
    public String $filter;
    public Integer $top;
    public Integer $skip;
    public Boolean $count = false;
    public final List<String> $orderby = new  ArrayList<String>();
    public final List<FieldExpression> $groupby = new ArrayList<FieldExpression>();

    public ClientDataServiceParams() {
        this.$top = 25;
        this.$skip = 0;
    }

    public HashMap<String,Object> toHashMap() {
        HashMap<String,Object> res = new HashMap<>();
        if ($filter != null) {
            res.put("$filter", this.$filter);
        }
        if (this.$select.size()>0) {
            res.put("$select", StringUtils.join(this.$select, ","));
        }
        if (this.$orderby.size()>0) {
            res.put("$orderby", StringUtils.join(this.$orderby, ","));
        }
        if (this.$groupby.size()>0) {
            res.put("$groupby", StringUtils.join(this.$groupby, ","));
        }
        if (this.$top>0 || this.$top==-1) {
            res.put("$top", this.$top.toString());
        }
        if (this.$skip>=0) {
            res.put("$skip", this.$skip.toString());
        }
        if (this.$count) {
            res.put("$count", true);
        }
        return res;
    }

}
