package org.most.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Parameter", namespace = EdmNamespace.DefaultNamespace)
public class EdmParameter {
    @JacksonXmlProperty(isAttribute=true)
    public String Name;
    @JacksonXmlProperty(isAttribute=true)
    public String Type;
    @JacksonXmlProperty(isAttribute=true)
    public Boolean Nullable = false;
}
