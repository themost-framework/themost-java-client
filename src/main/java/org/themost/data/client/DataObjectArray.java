package org.themost.data.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class DataObjectArray extends ArrayList<DataObject> {
    /**
     * Gets or sets the total number of objects satisfying the given query
     */
    public int total;
    /**
     * Gets or sets the number of skipped objects
     */
    public int skip;

    public static DataObjectArray create(JSONArray src) {
        DataObjectArray res = new DataObjectArray();
        if (src == null) {
            return res;
        }
        ListIterator iterator = src.listIterator();
        int k = 0;
        while (iterator.hasNext()) {
            res.add(DataObject.fromJSON((JSONObject) iterator.next()));
            k += 1;
        }
        return res;
    }

    public JSONArray toJSON() {
        JSONArray res = new JSONArray();
        Iterator<DataObject> k = this.iterator();
        while(k.hasNext()) {
            res.add(k.next().toJSON());
        }
        return res;
    }

}
