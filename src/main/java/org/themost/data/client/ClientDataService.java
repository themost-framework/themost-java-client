package org.themost.data.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 
 */
public class ClientDataService {

    public URI uri;

    private final Map<String, String> _headers = new HashMap<String, String>();

    public ClientDataService(String host) throws URISyntaxException {
        //set host URI
        this.uri = new URI(host);
    }

    public Map<String,String> getHeaders() {
        return this._headers;
    }

    public ClientDataService setHeader(String header, String value) {
        this._headers.put(header, value);
        return this;
    }

    public ClientDataService(URI host) {
        //set host URI
        this.uri = host;
    }

    public ClientDataQueryable model(String model) {
        return new ClientDataQueryable(this, model);
    }
    /**
     * @return ClientDataQueryable
     */
    public Object execute(DataServiceExecuteOptions options) throws IOException {
        HttpRequestBase request;
        String url = this.uri.resolve(options.url).toString();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        switch (options.method.toUpperCase(Locale.ROOT)) {
            case "PUT":
                request = new HttpPut(url);
                break;
            case "POST":
                request = new HttpPost(url);
                break;
            case "HEAD":
                request = new HttpHead(url);
                break;
            case "OPTIONS":
                request = new HttpOptions(url);
                break;
            case "DELETE":
                request = new HttpDelete(url);
                break;
            default:
                request = new HttpGet(url);
                break;
        }
        Map<String, String> serviceHeaders = this.getHeaders();
        serviceHeaders.keySet().forEach(header -> {
            request.setHeader(header, serviceHeaders.get(header));
        });
        if (options.headers != null) {
            options.headers.keySet().forEach(header -> {
                request.setHeader(header, options.headers.get(header));
            });
        }
        CloseableHttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        // no content
        if (statusLine.getStatusCode() != 204) {
            return null;
        }
        // get body
        HttpEntity bodyEntity = response.getEntity();
        String body = EntityUtils.toString(bodyEntity);
        response.close();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(body);
    }

    /**
     * @return
     */
    public Object get(DataServiceExecuteOptions options) throws URISyntaxException, IOException {
        options.method = "GET";
        return this.execute(options);
    }

    /**
     * @return
     */
    public Object post(DataServiceExecuteOptions options) throws URISyntaxException, IOException {
        options.method = "POST";
        return this.execute(options);
    }

    /**
     * @return
     */
    public Object put(DataServiceExecuteOptions options) throws URISyntaxException, IOException {
        options.method = "PUT";
        return this.execute(options);
    }
}


