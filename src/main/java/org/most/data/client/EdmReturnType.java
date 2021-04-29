package org.most.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ReturnType", namespace = EdmNamespace.DefaultNamespace)
public class EdmReturnType {
    @JacksonXmlProperty(isAttribute=true)
    public String Type;
    @JacksonXmlProperty(isAttribute=true)
    public Boolean Nullable = true;
}
