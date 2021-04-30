package org.themost.data.client;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientDataQueryableTest {

    private static final String HOST_URI = "http://localhost:3000/api/";
    private static final String AUTH_URI = "http://localhost:3000/auth/token";
    private static final String TEST_USERNAME = "alexis.rees@example.com";
    private static final String TEST_PASSWORD = "secret";

    public ClientDataContext context;

    @BeforeAll
    void BeforeAll() throws URISyntaxException, IOException {
        this.context = new ClientDataContext(HOST_URI);
        TestTokenResult token = TestUtils.getTestToken(AUTH_URI, TEST_USERNAME, TEST_PASSWORD);
        this.context.setBearerAuthorization(token.access_token);
    }

    @Test
    public void ShouldGetItems() throws IOException, URISyntaxException {
        Object result = this.context.model("Products")
                .where("category").equal("Laptops")
                .orderBy("price")
                .take(5)
                .getItems();
        assertNotNull(result);
        assertTrue(result instanceof ArrayNode);
        ArrayNode items = (ArrayNode)result;
        items.forEach(item -> {
            assertEquals(item.get("category").asText(), "Laptops");
        });
    }

    @Test
    public void ShouldUseAsQueryable() throws IOException, URISyntaxException {
        Object result = context.model("Orders")
                .asQueryable()
                .select("id","customer/description as customerDescription",
                        "orderDate",
                        "orderedItem/name as orderedItemName")
                .where("paymentMethod/alternateName").equal("DirectDebit")
                .orderByDescending("orderDate")
                .take(10)
                .getItems();
        assertNotNull(result);
        assertTrue(result instanceof ArrayNode);
        ArrayNode items = (ArrayNode)result;
        items.forEach(item -> {
            assertTrue(item.has("customerDescription"));
            assertTrue(item.has("orderDate"));
        });
    }

    @Test
    public void ShouldUseSelect() throws IOException, URISyntaxException {
        Object result = context.model("Orders")
                .select("id","customer","orderedItem","orderStatus")
                .orderBy("orderDate")
                .take(25)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        items.forEach(item -> {
            assertTrue(item.has("id"));
            assertTrue(item.has("customer"));
            assertFalse(item.has("orderNumber"));
        });
    }

    @Test
    public void ShouldUseTake() throws IOException, URISyntaxException {
        Object result = context.model("Orders")
                .asQueryable()
                .take(10)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        assertEquals(10, items.size());
    }

    @Test
    public void ShouldUseSkip() throws IOException, URISyntaxException {
        Object result = context.model("Orders")
                .asQueryable()
                .skip(10)
                .take(5)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        assertEquals(5, items.size());
    }

    @Test
    public void ShouldUseOr() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("category").equal("Desktops")
                .or("category").equal("Laptops")
                .orderBy("price")
                .take(5)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        items.forEach(item -> {
            assertTrue(item.get("category").asText().equals("Desktops") ||
                    item.get("category").asText().equals("Laptops"));
        });
    }

    @Test
    public void ShouldUseAnd() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("category").equal("Laptops")
                .and("price").greaterThan(200)
                .orderBy("price")
                .take(5)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        items.forEach(item -> {
            assertTrue(item.get("category").asText().equals("Laptops") &&
                    item.get("price").asDouble() > 200);
        });
    }


}
