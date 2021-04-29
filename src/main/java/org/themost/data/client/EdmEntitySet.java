package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "EntitySet", namespace = EdmNamespace.DefaultNamespace)
public class EdmEntitySet {
    @JacksonXmlProperty(isAttribute=true)
    public String Name;
    @JacksonXmlProperty(isAttribute=true)
    public String EntityType;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = EdmNamespace.DefaultNamespace)
    public List<EdmAnnotation> Annotations = new ArrayList<EdmAnnotation>();

    public String getResourcePath() {
        EdmAnnotation annotation = this.Annotations.stream().filter(edmAnnotation -> {
           return EdmTerm.ResourcePath.equals(edmAnnotation.Term);
        }).findFirst().orElse(null);
        if (annotation != null) {
            return annotation.StringValue;
        }
        return null;
    }

    public void setResourcePath(String value) {
        EdmAnnotation annotation = this.Annotations.stream().filter(edmAnnotation -> {
            return EdmTerm.ResourcePath.equals(edmAnnotation.Term);
        }).findFirst().orElse(null);
        if (annotation != null) {
            annotation.StringValue = value;
        } else {
            this.Annotations.add(new EdmAnnotation() {{
                Term = EdmTerm.ResourcePath;
                StringValue = value;
            }});
        }
    }

}
