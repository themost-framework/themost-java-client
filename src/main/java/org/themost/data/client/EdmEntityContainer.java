package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "EntityContainer", namespace = EdmNamespace.DefaultNamespace)
public class EdmEntityContainer {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmEntitySet> EntitySet = new ArrayList<EdmEntitySet>();
}
