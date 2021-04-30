package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Edmx", namespace = EdmNamespace.EdmxNamespace)
public class EdmxRoot {
    @JacksonXmlProperty(isAttribute=true)
    public String Version;
    @JacksonXmlProperty()
    public EdmxDataServices DataServices;
}
