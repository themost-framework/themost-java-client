package org.themost.data.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.security.InvalidKeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class DataObject extends HashMap<String,Object> {

    public DataObject prop(String name) throws InvalidKeyException {
        if (this.containsKey(name)) {
            Object value = this.get(name);
            if (value instanceof DataObject) {
                return (DataObject)value;
            }
            throw new InvalidKeyException();
        }
        return null;
    }

    public int getInteger(String name) {
        if (this.containsKey(name)) {
            Object value = this.get(name);
            return (Integer)value;
        }
        return 0;
    }

    public float getFloat(String name) {
        if (this.containsKey(name)) {
            Object value = this.get(name);
            return (Float)value;
        }
        return 0;
    }

    public double getDouble(String name) {
        if (this.containsKey(name)) {
            Object value = this.get(name);
            return (Double)value;
        }
        return 0;
    }

    public String getString(String name) {
        if (this.containsKey(name)) {
            Object value = this.get(name);
            return (String)value;
        }
        return null;
    }

    public Date getDate(String name) {
        if (this.containsKey(name)) {
            Object value = this.get(name);
            return (Date)value;
        }
        throw null;
    }

    public Boolean getBoolean(String name) {
        if (this.containsKey(name)) {
            Object value = this.get(name);
            return (Boolean)value;
        }
        return false;
    }


    private static String DateTimeRegex ="^(\\d{4})-(0[1-9]|1[0-2])-(\\d{2})(\\s)(\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})[-+](\\d{2}):(\\d{2})$";
    //"^(\\d{4})(?:-?W(\\d+)(?:-?(\\d+)D?)?|(?:-(\\d+))?-(\\d+))(?:[T ](\\d+):(\\d+)(?::(\\d+)(?:\\.(\\d+))?)?)?(?:Z(-?\\d*))?$";

    private static boolean isDate(Object value) {
        if (value instanceof String) {
            return ((String)value).matches(DateTimeRegex);
        }
        return false;
    }

    private static SimpleDateFormat ISODateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");

    public JSONObject toJSON() {
        return DataObject.toJSON(this);
    }

    public static JSONObject toJSON(DataObject src) {
        JSONObject output = new JSONObject();
        src.forEach((k,v) -> {
            if (v instanceof DataObject) {
                output.put(k, DataObject.toJSON((DataObject)v));
            }
            else if (v instanceof Date) {
                output.put(k, ISODateFormatter.format((Date)v));
            }
            else {
                output.put(k,v);
            }
        });
        return output;
    }

    public static DataObject create(JSONObject src) {
        return DataObject.fromJSON(src);
    }

    public static DataObject fromJSON(JSONObject src) {
        if (src == null) {
            return null;
        }
        DataObject res = new DataObject();
        Iterator keyIterator = src.keySet().iterator();
        String key;
        Object value;
        while (keyIterator.hasNext()) {
            key = (String)keyIterator.next();
            if (src.optJSONArray(key) != null) {
                res.put(key, DataObjectArray.create((JSONArray) src.get(key)));
            }
            else if (src.optJSONObject(key) != null) {
                res.put(key, DataObject.fromJSON((JSONObject) src.get(key)));
            }
            else {
                value = src.get(key);
                if (isDate(value)) {
                    try {
                        res.put(key,ISODateFormatter.parse((String)value));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        res.put(key, value);
                    }
                }
                else {
                    res.put(key, value);
                }
            }
        }
        return res;
    }
}
