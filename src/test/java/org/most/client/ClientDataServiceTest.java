package org.most.client;

import org.apache.commons.beanutils.DynaBean;
import org.apache.http.HttpException;
import org.junit.Test;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by kbarbounakis on 12/17/15.
 */
public class ClientDataServiceTest {

    @Test
    public void testCallPersonService() {
        try {
            ClientDataService svc = new ClientDataService("http://127.0.0.1:3004/");
            try {
                //authenticate user
                ClientDataResultSet result = svc.authenticate("alexis.rees@example.com","user").model("Person").take(5);
                System.out.println(result.items[0].get("familyName") + " " + result.items[0].get("givenName"));
                System.out.println(result.items[0].get("dateModified"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (HttpException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}