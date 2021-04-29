package org.most.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(namespace = EdmNamespace.DefaultNamespace)
public class EdmProcedure {
    @JacksonXmlProperty(isAttribute=true)
    public String Name;
    @JacksonXmlProperty(isAttribute=true)
    public Boolean IsBound = true;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmParameter> Parameter = new ArrayList<EdmParameter>();
    @JacksonXmlProperty(localName = "ReturnType", namespace = EdmNamespace.DefaultNamespace)
    public EdmReturnType ReturnType;
}
