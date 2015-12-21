package org.most.client;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.io.Writer;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class DataObject extends HashMap<String,Object> implements JSON {

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

    private static SimpleDateFormat ISODateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");

    public JSONObject toJSONObject() {
        JSONObject output = new JSONObject();
        this.forEach((k,v) -> {
            if (v instanceof DataObject) {
                output.put(k, ((DataObject)v).toJSONObject());
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

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public String toString(int i) {
        return this.toJSONObject().toString(i);
    }

    @Override
    public String toString(int i, int i1) {
        return this.toJSONObject().toString(i, i1);
    }

    @Override
    public Writer write(Writer writer) {
        return this.toJSONObject().write(writer);
    }
}
