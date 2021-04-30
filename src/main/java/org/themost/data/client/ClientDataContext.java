package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;

public class ClientDataContext {

    private final ClientDataService _service;
    private EdmSchema _schema;


    public ClientDataContext(String host) throws URISyntaxException {
        this._service = new ClientDataService(host);
    }

    /**
     * Gets the client service associated with this context
     * @return ClientDataService
     */
    public ClientDataService getService() {
        return this._service;
    }

    /**
     * Returns a client data model based on the given name
     * @param name
     * @return ClientDataModel
     */
    public ClientDataModel model(String name) {
        return new ClientDataModel(name);
    }

    /**
     * Returns a client data model based on the given object type
     * @param type Class<?>
     * @return ClientDataModel
     */
    public ClientDataModel model(Class<?> type) {

        return new ClientDataModel(type.getName());
    }

    /**
     * Prepares data service for bearer authorization
     * @param bearerToken String
     * @return this
     */
    public ClientDataContext setBearerAuthorization(String bearerToken) {
        Map<String,String> headers = this._service.getHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, "Bearer ".concat(bearerToken));
        return this;
    }

    /**
     * Prepares data service for basic authorization
     * @param username String
     * @param password String
     * @return this
     */
    public ClientDataContext setBasicAuthorization(String username, String password) {

        Map<String,String> headers = this._service.getHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, "Basic ".concat(new String(Base64.getEncoder().encode((username + ":" + password).getBytes()))));
        return this;
    }

    public EdmSchema getMetadata(Boolean force) throws IOException {
        if (!force && this._schema != null) {
            return this._schema;
        }
        String url = this._service.uri.resolve("./$metadata").toString();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        Map<String, String> serviceHeaders = this._service.getHeaders();
        serviceHeaders.keySet().forEach(header -> {
            httpGet.setHeader(header, serviceHeaders.get(header));
        });
        CloseableHttpResponse response = httpClient.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        // get body
        HttpEntity bodyEntity = response.getEntity();
        String result = EntityUtils.toString(bodyEntity);
        response.close();
        XmlMapper xmlMapper = new XmlMapper();
        EdmxRoot root = xmlMapper.readValue(result, EdmxRoot.class);
        return root.DataServices.Schema;

    }

    public EdmSchema getMetadata() throws IOException {
        return this.getMetadata(false);
    }


}
