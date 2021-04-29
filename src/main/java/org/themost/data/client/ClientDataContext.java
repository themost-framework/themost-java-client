package org.themost.data.client;

import org.apache.http.HttpHeaders;

import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;

public class ClientDataContext {

    private final ClientDataService _service;


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


}
