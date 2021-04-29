package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "PropertyRef", namespace = EdmNamespace.DefaultNamespace)
public class EdmPropertyRef {
    @JacksonXmlProperty(isAttribute=true)
    public String Name;
}
