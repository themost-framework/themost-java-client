package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "EntityType", namespace = EdmNamespace.DefaultNamespace)
public class EdmEntityType {
    @JacksonXmlProperty(isAttribute=true)
    public String Name;
    @JacksonXmlProperty(isAttribute=true)
    public String BaseType;
    @JacksonXmlProperty(isAttribute=true)
    public boolean OpenType;
    @JacksonXmlProperty(localName = "ReturnType", namespace = EdmNamespace.DefaultNamespace)
    public EdmKey Key;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmProperty> Property = new ArrayList<EdmProperty>();
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmNavigationProperty> NavigationProperty = new ArrayList<EdmNavigationProperty>();
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmAnnotation> Annotations = new ArrayList<EdmAnnotation>();
}
