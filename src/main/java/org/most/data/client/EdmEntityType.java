package org.most.data.client;

import java.util.ArrayList;
import java.util.List;

public class EdmEntityType {
    public String Name;
    public String BaseType;
    public boolean OpenType;
    public EdmKey Key;
    public List<EdmProperty> Property = new ArrayList<EdmProperty>();
    public List<EdmNavigationProperty> NavigationProperty = new ArrayList<EdmNavigationProperty>();
    public String ImplementsType;
    public List<EdmAnnotation> Annotations = new ArrayList<EdmAnnotation>();
}
