package org.most.data.client;

import java.util.ArrayList;
import java.util.List;

public class EdmSchema {
    public List<EdmEntityType> EntityType = new ArrayList<EdmEntityType>();
    public EdmEntityContainer EntityContainer = new EdmEntityContainer();
    public List<EdmAction> Action = new ArrayList<EdmAction>();
    public List<EdmFunction> Function = new ArrayList<EdmFunction>();
}
