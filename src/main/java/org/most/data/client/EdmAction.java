package org.most.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Action", namespace = EdmNamespace.DefaultNamespace)
public class EdmAction extends EdmProcedure {
}
