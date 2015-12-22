package org.most.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.text.*;
import java.text.ParseException;
import java.util.*;

import net.sf.json.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
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
    private Object execute(ServiceMethod method, String relativeUri, HashMap<String,Object> query, Object data) throws URISyntaxException, IOException {
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

        if (request instanceof HttpEntityEnclosingRequestBase && data != null) {
            StringEntity dataEntity;
            if (data instanceof DataObject) {
                dataEntity =new StringEntity(JSONSerializer.toJSON(((DataObject)data).toJSON()).toString());
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
            Object result;
            if (body.length()==0) {
                return null;
            }
            if (body.matches("^(\\s+|\\n+)?\\[.*\\](\\s+|\\n+)?$")) {
                result = JSONArray.fromObject(body);
            }
            else {
                result = JSONObject.fromObject(body);
            }
            if (result instanceof JSONArray) {
                return DataObjectArray.create((JSONArray)result);
            }
            else if (result instanceof JSONObject) {
                JSONObject o = (JSONObject)result;
                //check if result follows ClientDataResultSet structure
                if (o.containsKey("total")
                        && o.containsKey("records")) {
                    DataObjectArray output = DataObjectArray.create(o.optJSONArray("records"));
                    output.total = o.optInt("total");
                    if (o.containsKey("skip"))
                        output.skip = o.optInt("skip");
                    return output;
                }
                else {
                    return DataObject.fromJSON(o);
                }
            }
        }
        else {
            throw new HttpResponseException(response.getStatusLine().getStatusCode(),response.getStatusLine().getReasonPhrase());
        }
        return null;
    }

    /**
     * @return
     */
    public Object get(String relativeUri, HashMap<String, Object> query) throws URISyntaxException, IOException {
        return this.execute(ServiceMethod.GET, relativeUri, query, null);
    }

    /**
     * @return
     */
    public Object post(String relativeUri, HashMap<String, Object> query, Object data) throws URISyntaxException, IOException {
        return this.execute(ServiceMethod.POST, relativeUri, query, data);
    }

    /**
     * @return
     */
    public Object put(String relativeUri, HashMap<String, Object> query, Object data) throws URISyntaxException, IOException {
        return this.execute(ServiceMethod.POST, relativeUri, query, data);
    }
}


