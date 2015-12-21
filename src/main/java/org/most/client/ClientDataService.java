package org.most.client;

import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.text.*;
import java.text.ParseException;
import java.util.*;

import net.sf.json.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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

public class ClientDataService {

    public URI uri;

    private String lang_;
    private String cookie_;

    public ClientDataService(String host) throws URISyntaxException {
        //set host URI
        this.uri = new URI(host);
    }

    public ClientDataService(URI host) {
        //set host URI
        this.uri = host;
    }

    public ClientDataService language(String lang) {
        this.lang_ = lang;
        return this;
    }

    public ClientDataQueryable model(String model) {
        return new ClientDataQueryable(this, model);
    }

    public ClientDataService authenticate(String cookie) {
        this.cookie_ = cookie;
        return this;
    }

    public ClientDataService authenticate(String username, String password) throws IOException, HttpException {
        HttpRequestBase request = new HttpGet(this.uri);
        request.addHeader("Authorization","Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes())));
        HttpClient client = new DefaultHttpClient();
        try {
            //execute response
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode()==200) {
                Header[] headers = response.getAllHeaders();
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].getName().equals("Set-Cookie")) {
                        this.cookie_ = headers[i].getValue();
                        return this;
                    }
                }
                this.cookie_ = null;
                return this;
            }
            else {
                throw new HttpException("An error occured");
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * @return
     */
    protected ClientDataResultSet execute(ServiceMethodName method, String relativeUri, HashMap<String,Object> query, Object data) throws URISyntaxException {
        HttpRequestBase request;
        URI testUri = URI.create(relativeUri);
        if (testUri.isAbsolute()) {
            throw new InvalidParameterException("The execution url is not valid.");
        }
        URI resolve = this.uri.resolve(relativeUri);
        URIBuilder builder = new URIBuilder(resolve);

        if (query != null && query.size()>0) {
            //enumerate data
            query.forEach((k,v) -> {
                builder.setParameter(k,v.toString());
            });
            //set request parameters
        }
        resolve = builder.build();

        switch (method) {
            case PUT:
                request = new HttpPut(resolve);
                break;
            case POST:
                request = new HttpPost(resolve);
                break;
            case GET:
                request = new HttpGet(resolve);
                break;
            case HEAD:
                request = new HttpHead(resolve);
                break;
            case OPTIONS:
                request = new HttpOptions(resolve);
                break;
            case DELETE:
                request = new HttpDelete(resolve);
                break;
            default:
                request = new HttpGet(resolve);
                break;
        }

        //set content type to application/json
        request.addHeader("Content-Type","application/json");
        //set accept language (if any)
        if (this.lang_ != null) {
            request.addHeader("Accept-Language",this.lang_);
        }
        //set cookie (if any)
        if (this.cookie_ != null) {
            request.addHeader("Cookie",this.cookie_);
        }

        HttpClient client = new DefaultHttpClient();
        try {
            if (request instanceof HttpEntityEnclosingRequestBase && data != null) {
                StringEntity dataEntity;
                if (data instanceof DataObject) {
                    dataEntity =new StringEntity(JSONSerializer.toJSON(((DataObject)data).toJSONObject()).toString());
                }
                else {
                    dataEntity =new StringEntity(JSONSerializer.toJSON(data).toString());
                }
                ((HttpEntityEnclosingRequestBase)request).setEntity(dataEntity);
            }
            //execute response
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode()==200) {
                String body =EntityUtils.toString(response.getEntity(),"UTF-8");
                Object result = JSONObject.fromObject(body);
                ClientDataResultSet output = new ClientDataResultSet();
                if (result instanceof JSONArray) {
                    output.records = JSONArrayToDataObject((JSONArray)result);
                    output.total = output.records.length;
                }
                else if (result instanceof JSONObject) {
                    JSONObject o = (JSONObject)result;
                    if (o.containsKey("total") && o.containsKey("records")) {
                        output.total = o.optInt("total");
                        if (o.containsKey("skip"))
                            output.skip = o.optInt("skip");
                        output.records = JSONArrayToDataObject(o.optJSONArray("records"));
                    }
                    else {
                        output.total = 1;
                        output.records = new DataObject[] { JSONObjectToDataObject((JSONObject)result) };
                    }
                }
                return output;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DataObject[] JSONArrayToDataObject(JSONArray src) {
        if (src == null) {
            return new DataObject[0];
        }
        DataObject[] res = new DataObject[src.size()];
        ListIterator iterator = src.listIterator();
        int k = 0;
        while (iterator.hasNext()) {
            res[k] = JSONObjectToDataObject((JSONObject) iterator.next());
            k += 1;
        }
        return res;
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


    private static DataObject JSONObjectToDataObject(JSONObject src) {
        DataObject res = new DataObject();
        Iterator keyIterator = src.keySet().iterator();
        String key;
        Object value;
        while (keyIterator.hasNext()) {
            key = (String)keyIterator.next();
            if (src.optJSONArray(key) != null) {
                res.put(key, JSONArrayToDataObject((JSONArray) src.get(key)));
            }
            else if (src.optJSONObject(key) != null) {
                res.put(key, JSONObjectToDataObject((JSONObject) src.get(key)));
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

    /**
     * @return
     */
    protected ClientDataResultSet get(String relativeUri, HashMap<String, Object> query) throws URISyntaxException {
        return this.execute(ServiceMethodName.GET, relativeUri, query, null);
    }

    /**
     * @return
     */
    protected ClientDataResultSet post(String relativeUri, HashMap<String, Object> query, Object data) throws URISyntaxException {
        return this.execute(ServiceMethodName.POST, relativeUri, query, data);
    }
}


