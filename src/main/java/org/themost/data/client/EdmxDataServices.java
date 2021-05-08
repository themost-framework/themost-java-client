package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "DataServices", namespace = EdmNamespace.EdmxNamespace)
public class EdmxDataServices {
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public EdmSchema Schema;
}
