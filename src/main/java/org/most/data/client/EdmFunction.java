package org.most.data.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Function", namespace = EdmNamespace.DefaultNamespace)
public class EdmFunction extends EdmProcedure {
}
