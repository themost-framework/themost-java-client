package org.themost.data.client;

import java.time.ZonedDateTime;

public class Thing {
    public int id;
    public String additionalType;
    public String name;
    public String alternateName;
    public String description;
    public ZonedDateTime dateCreated;
    public ZonedDateTime dateModified;
    public Object createdBy;
    public Object modifiedBy;
}
