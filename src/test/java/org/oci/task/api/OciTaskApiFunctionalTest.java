package org.oci.task.api;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @brief Functional tests for OCI Task Service APIs.
 * @author rajeshkurup@live.com
 */
@SpringBootTest(classes = OciTaskServTestMain.class)
public class OciTaskApiFunctionalTest {

    private RequestSpecification httpRequest;

    @BeforeEach
    public void init() {
        RestAssured.baseURI = "http://192.9.237.204:8081/v1/ocitaskserv";
        httpRequest = RestAssured.given();
    }

    @Test
    public void testGetTasksSuccess() throws JSONException {
        Response httpResp = httpRequest.request(Method.GET, "/tasks");

        Assertions.assertEquals(200, httpResp.getStatusCode());

        JSONObject apiResp = new JSONObject(httpResp.getBody().print());
        JSONArray tasks = apiResp.getJSONArray("tasks");

        Assertions.assertTrue(tasks.length() > 0);
        Assertions.assertTrue(tasks.getJSONObject(0).getLong("id") > 0);
        Assertions.assertTrue(tasks.getJSONObject(0).getString("title").length() > 0);
    }

}
