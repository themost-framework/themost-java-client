package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    public void ShouldGetMetadata() throws IOException, URISyntaxException {
        TestTokenResult result = TestUtils.getTestToken("http://localhost:3000/auth/token",
                "alexis.rees@example.com",
                "secret");
        assertNotNull(result);
        assertNotNull(result.access_token);
        ClientDataContext context = new ClientDataContext("http://localhost:3000/api/");
        context.setBearerAuthorization(result.access_token);
        EdmSchema schema = context.getMetadata();
        assertNotNull(schema);
        assertNotNull(schema.EntityContainer);
    }
}
