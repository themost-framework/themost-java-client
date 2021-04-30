package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "NavigationProperty", namespace = EdmNamespace.DefaultNamespace)
public class EdmNavigationProperty {
    @JacksonXmlProperty(isAttribute=true)
    public String Name;
    @JacksonXmlProperty(isAttribute=true)
    public String Type;
    @JacksonXmlProperty(isAttribute=true)
    public Boolean Nullable = true;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Annotation", namespace = EdmNamespace.DefaultNamespace)
    public List<EdmAnnotation> Annotations = new ArrayList<EdmAnnotation>();
}
