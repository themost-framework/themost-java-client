package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "Schema", namespace = EdmNamespace.DefaultNamespace)
public class EdmSchema {

    @JacksonXmlProperty(isAttribute=true)
    public String Namespace;

    @JacksonXmlProperty(isAttribute=true)
    public String Alias;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmEntityType> EntityType = new ArrayList<EdmEntityType>();

    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public EdmEntityContainer EntityContainer = new EdmEntityContainer();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmAction> Action = new ArrayList<EdmAction>();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmFunction> Function = new ArrayList<EdmFunction>();
}
