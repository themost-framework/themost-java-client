package org.themost.data.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

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
    }
}
