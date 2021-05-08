package org.themost.data.client;

/**
 *
 */
public class ClientDataModel {

    private final String _name;
    public ClientDataContext context;

    public ClientDataModel(String name) {
        this._name = name;
    }

    public ClientDataModel(Class<?> type) {
        this._name = type.getName();
    }

    /**
     * Returns the name of the current data model
     * @return String
     */
    public String getName() {
        return this._name;
    }

    /**
     * Returns a client data queryable instance of the given model
     * @return ClientDataQueryable
     */
    public ClientDataQueryable asQueryable() {
        return new ClientDataQueryable(this.context.getService(), this.getName());
    }

    /**
     *
     * @param name String
     * @return ClientDataQueryable
     */
    public ClientDataQueryable where(String name) {
        return this.asQueryable().where(name);
    }

    /**
     *
     * @param name String
     * @return ClientDataQueryable
     */
    public ClientDataQueryable select(String ... name) {
        return this.asQueryable().select(name);
    }


}
