package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Annotation", namespace = EdmNamespace.DefaultNamespace)
public class EdmAnnotation {
    @JacksonXmlProperty(isAttribute=true)
    public String Term;
    @JacksonXmlProperty(isAttribute=true, localName = "String")
    public String StringValue;
    @JacksonXmlProperty(isAttribute=true)
    public String Tag;
    @JacksonXmlProperty(isAttribute=true, localName = "Bool")
    public Boolean BoolValue;
}
