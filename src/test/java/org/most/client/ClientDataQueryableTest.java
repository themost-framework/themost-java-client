package org.most.client;

import net.sf.json.JSONObject;
import org.apache.http.HttpException;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by kbarbounakis on 12/20/15.
 */
public class ClientDataQueryableTest {

    private static final String HOST_URI = "http://127.0.0.1:3004/";
    private static final String TEST_USERNAME = "alexis.rees@example.com";
    private static final String TEST_PASSWORD = "user";

    private ClientDataQueryable testGetQueryable(String model) throws URISyntaxException, IOException, HttpException {
        ClientDataService svc = new ClientDataService(HOST_URI);
        return svc.authenticate(TEST_USERNAME,TEST_PASSWORD).model(model);
    }

    @Test
    public void testSimpleFilter1() throws HttpException, IOException, URISyntaxException {

        ClientDataQueryable q = testGetQueryable("Person");
        DataObject result = q.where("id").equal(353).first();
        result.put("familyName","Rees");
        q.save(result);
    }

    @Test
    public void testSelect() throws HttpException, IOException, URISyntaxException {

        ClientDataQueryable q = testGetQueryable("Person");
        DataObject result = q.select("id", "givenName", "familyName")
                    .where("id").equal(257).first();

    }

    @Test
    public void testSelectWithAlias() throws HttpException, IOException, URISyntaxException {

        ClientDataQueryable q = testGetQueryable("Person");
        DataObject result = q.select("id").alsoSelect(FieldExpression.create("familyName").as("name"))
                .where("id").equal(257).first();




    }

    @Test
    public void testSelectCount() throws HttpException, IOException, URISyntaxException {

        ClientDataQueryable q = testGetQueryable("Person");
        Object result = q.select(FieldExpression.create("id").count().as("personCount")).value();

    }

}
