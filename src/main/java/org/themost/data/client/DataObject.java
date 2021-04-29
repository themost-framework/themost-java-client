package org.themost.data.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.security.InvalidKeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
