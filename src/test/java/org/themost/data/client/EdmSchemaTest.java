package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class EdmSchemaTest {
    @Test
    public void ShouldSerializeEdmAction() throws IOException {
        EdmAction action = new EdmAction() {{
            Name = "claim";
            ReturnType = new EdmReturnType() {{
                Type = "Action";
            }};
        }};
        action.Parameter.add(new EdmParameter() {{
            Name = "bindingParameter";
            Type = "Action";
        }});
        XmlMapper xmlMapper = new XmlMapper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlMapper.writeValue(outputStream, action);
        outputStream.close();
        String xml = outputStream.toString();
        System.out.println(xml);
        action = xmlMapper.readValue(xml, EdmAction.class);
        assertNotNull(action);
        assertEquals(action.Name, "claim");
        assertEquals(action.ReturnType.Type, "Action");
        assertEquals(action.Parameter.size(), 1);

    }
    @Test
    public void ShouldSerializeEdmFunction() throws IOException {
        EdmFunction func = new EdmFunction() {{
            Name = "Me";
            ReturnType = new EdmReturnType() {{
                Type = "User";
            }};
        }};
        func.Parameter.add(new EdmParameter() {{
            Name = "bindingParameter";
            Type = "CollectionOf(User)";
        }});
        XmlMapper xmlMapper = new XmlMapper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlMapper.writeValue(outputStream, func);
        outputStream.close();
        String xml = outputStream.toString();
        System.out.println(xml);
        func = xmlMapper.readValue(xml, EdmFunction.class);
        assertNotNull(func);
        assertEquals(func.Name, "Me");
        assertEquals(func.ReturnType.Type, "User");
        assertEquals(func.Parameter.size(), 1);

    }
}
