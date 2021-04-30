package org.themost.data.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    static TestTokenResult getTestToken(String url, String username, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // get test api
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        List<NameValuePair> form = new ArrayList<NameValuePair>();

        form.add(new BasicNameValuePair("username", username));
        form.add(new BasicNameValuePair("password", password));
        form.add(new BasicNameValuePair("client_id", "9165351833584149"));
        form.add(new BasicNameValuePair("client_secret", "hTgqFBUhCfHs/quf/wnoB+UpDSfUusKA"));
        form.add(new BasicNameValuePair("scope", "profile"));
        form.add(new BasicNameValuePair("grant_type", "password"));
        httpPost.setEntity(new UrlEncodedFormEntity(form));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        // get body
        HttpEntity body = response.getEntity();
        String result = EntityUtils.toString(body);
        response.close();
        // return
        return new ObjectMapper().readValue(result, TestTokenResult.class);
    }

}
