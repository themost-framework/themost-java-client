package org.themost.data.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
        items.forEach(item -> assertEquals(item.get("category").asText(), "Laptops"));
    }

    @Test
    public void ShouldGetTypedItems() throws IOException, URISyntaxException {
        List<Product> items = this.context.model("Products")
                .where("category").equal("Laptops")
                .orderBy("price")
                .take(5)
                .getItems(Product.class);
        assertNotNull(items);
        items.forEach(item -> assertEquals(item.category, "Laptops"));
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
        items.forEach(item -> assertTrue(item.get("category").asText().equals("Desktops") ||
                item.get("category").asText().equals("Laptops")));
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
        for (JsonNode item : items) {
            assertTrue(item.get("category").asText().equals("Laptops") &&
                    item.get("price").asDouble() > 200);
        }
    }

    @Test
    public void ShouldUseGetItem() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("name").equal("Google Chromebook Pixel")
                .getItem();
        JsonNode item = (JsonNode)result;
        assertNotNull(item);
    }

    @Test
    public void ShouldUseGetTypedItem() throws IOException, URISyntaxException {
        Product result = context.model("Products")
                .where("name").equal("Google Chromebook Pixel")
                .getItem(Product.class);
        assertNotNull(result);
    }

    @Test
    public void ShouldUseGreaterThan() throws IOException, URISyntaxException {
        Object result = context.model("Orders")
                .where("orderedItem/price").greaterThan(968)
                .and("orderedItem/category").equal("Laptops")
                .and("orderStatus/alternateName").notEqual("OrderCancelled")
                .select("id",
                        "orderStatus/name as orderStatusName",
                        "customer/description as customerDescription",
                        "orderedItem/price as orderItemPrice")
                .orderByDescending("orderDate")
                .take(5)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        items.forEach(item -> {
            double price = item.get("orderItemPrice").asDouble();
            assertTrue(price > (double) 968);
        });
    }

    @Test
    public void ShouldUseGreaterOrEqual() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("price").greaterThan(968)
                .orderBy("price")
                .take(10)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.get("price").asDouble() >= (double) 968);
        }
    }

    @Test
    public void ShouldUseBetween() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("category").equal("Laptops")
                .and("price").between(200,750)
                .orderBy("price")
                .take(5)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.get("price").asDouble() >= (double) 200 &&
                    item.get("price").asDouble() <= (double) 750);
        }
    }

    @Test
    public void ShouldUseEqual() throws IOException, URISyntaxException {
        Object result = context.model("Orders")
                .where("id").equal(10)
                .getItem();
        JsonNode item = (JsonNode)result;
        assertEquals(10, item.get("id").asInt());
        result = context.model("Orders")
                .where("id").eq(10)
                .getItem();
        item = (JsonNode)result;
        assertEquals(10, item.get("id").asInt());
    }

    @Test
    public void ShouldAssignFilter() throws IOException, URISyntaxException {
        Object result = context.model("Orders")
                .asQueryable().filter("id eq 10")
                .getItem();
        JsonNode item = (JsonNode)result;
        assertEquals(10, item.get("id").asInt());
    }

    @Test
    public void ShouldUseLowerThan() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("price").lowerThan(968)
                .orderBy("price")
                .take(5)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.get("price").asDouble() <= (double) 968);
        }
        result = context.model("Products")
                .where("price").lt(968)
                .orderBy("price")
                .take(5)
                .getItems();
        items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.get("price").asDouble() <= (double) 968);
        }
    }

    @Test
    public void ShouldUseCount() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .select("category", "count(id) as total")
                .groupBy("category")
                .orderByDescending("count(id)")
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.has("total"));
        }
    }

    @Test
    public void ShouldUseMax() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .select("category", "max(price) as maximumPrice")
                .where("category").equal("Laptops")
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.has("maximumPrice"));
        }
    }

    @Test
    public void ShouldUseMin() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .select("category", "min(price) as minimumPrice")
                .where("category").equal("Laptops")
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.has("minimumPrice"));
        }
    }

    @Test
    public void ShouldUseIndexOf() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("name").indexOf("Intel")
                .ge(0)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.get("name").asText().contains("Intel"));
        }
    }

    @Test
    public void ShouldUseSubstring() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("name").substring(6,4)
                .equal("Core")
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertEquals("Core", item.get("name").asText().substring(6, 10));
        }
    }

    @Test
    public void ShouldUseStartsWith() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("name").startsWith("Intel Core")
                .equal(true)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.get("name").asText().startsWith("Intel Core"));
        }
    }

    @Test
    public void ShouldUseEndsWith() throws IOException, URISyntaxException {
        Object result = context.model("Products")
                .where("name").endsWith("Edition")
                .equal(true)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertTrue(item.get("name").asText().endsWith("Edition"));
        }
    }

    @Test
    public void ShouldQueryByDate() throws IOException, URISyntaxException, ParseException {
        Object result = context.model("Orders")
                .where("orderDate").getDate()
                .equal("2019-02-10")
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.get("orderDate").asText());
            GregorianCalendar date = GregorianCalendar.from(zonedDateTime);
            assertEquals(2019, date.get(GregorianCalendar.YEAR));
            assertEquals(1, date.get(GregorianCalendar.MONTH));
            assertEquals(10, date.get(GregorianCalendar.DATE));
        }
    }

    @Test
    public void ShouldUseGetMonth() throws IOException, URISyntaxException, ParseException {
        Object result = context.model("Orders")
                .where("orderDate").getMonth()
                .equal(4).getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.get("orderDate").asText());
            GregorianCalendar date = GregorianCalendar.from(zonedDateTime);
            assertEquals(3, date.get(GregorianCalendar.MONTH));
        }
    }

    @Test
    public void ShouldUseGetDay() throws IOException, URISyntaxException, ParseException {
        Object result = context.model("Orders")
                .where("orderDate").getMonth()
                .equal(4)
                .and("orderDate").getDay()
                .equal(15).getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.get("orderDate").asText());
            GregorianCalendar date = GregorianCalendar.from(zonedDateTime);
            assertEquals(15, date.get(GregorianCalendar.DAY_OF_MONTH));
        }
    }

    @Test
    public void ShouldUseGetYear() throws IOException, URISyntaxException, ParseException {
        Object result = context.model("Orders")
                .where("orderDate").getMonth().equal(5)
                .and("orderDate").getDay().lowerOrEqual(10)
                .and("orderDate").getYear().equal(2015)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.get("orderDate").asText());
            GregorianCalendar date = GregorianCalendar.from(zonedDateTime);
            assertEquals(2015, date.get(GregorianCalendar.YEAR));
        }
    }

    @Test
    public void ShouldUseGetHours() throws IOException, URISyntaxException, ParseException {
        Object result = context.model("Orders")
                .where("orderDate").getMonth().equal(5)
                .and("orderDate").getDay().lowerOrEqual(10)
                .and("orderDate").getHours().between(10,18)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.get("orderDate").asText());
            GregorianCalendar date = GregorianCalendar.from(zonedDateTime);
            assertTrue(date.get(GregorianCalendar.HOUR) >= 10 &&
                    date.get(GregorianCalendar.HOUR) <= 18);
        }
    }

    @Test
    public void ShouldUseGetMinutes() throws IOException, URISyntaxException, ParseException {
        Object result = context.model("Orders")
                .where("orderDate").getMonth().equal(5)
                .and("orderDate").getHours().between(9,17)
                .and("orderDate").getMinutes().between(1,30)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.get("orderDate").asText());
            GregorianCalendar date = GregorianCalendar.from(zonedDateTime);
            assertTrue(date.get(GregorianCalendar.MINUTE) >= 1 &&
                    date.get(GregorianCalendar.MINUTE) <= 30);
        }
    }

    @Test
    public void ShouldUseGetSeconds() throws IOException, URISyntaxException, ParseException {
        Object result = context.model("Orders")
                .where("orderDate").getMonth().equal(5)
                .and("orderDate").getHours().between(9,17)
                .and("orderDate").getMinutes().between(1,30)
                .and("orderDate").getSeconds().between(1,45)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.get("orderDate").asText());
            GregorianCalendar date = GregorianCalendar.from(zonedDateTime);
            assertTrue(date.get(GregorianCalendar.SECOND) >= 1 &&
                    date.get(GregorianCalendar.SECOND) <= 45);
        }
    }

    @Test
    public void ShouldUseExpand() throws IOException, URISyntaxException {
        Object result = context.model("Orders")
                .asQueryable().orderByDescending("orderDate")
                .expand("customer")
                .take(10)
                .getItems();
        ArrayNode items = (ArrayNode)result;
        for (JsonNode item : items) {
            assertNotNull(item.get("customer"));
            assertNotNull(item.get("customer").get("jobTitle").asText());
        }
    }

    @Test
    public void ShouldUseClass() throws IOException, URISyntaxException {
        Product result = context.model(Product.class)
                .where("name").equal("Google Chromebook Pixel")
                .getItem(Product.class);
        assertNotNull(result);
    }


}
