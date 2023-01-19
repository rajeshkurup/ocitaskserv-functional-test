package org.oci.task.api;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @brief Functional tests for OCI Task Service APIs.
 * @author rajeshkurup@live.com
 */
@SpringBootConfiguration
@SpringBootTest
public class OciTaskApiFunctionalTest {

    private RequestSpecification httpRequest;

    @BeforeEach
    public void init() {
        RestAssured.baseURI = "http://192.9.237.204:8081/v1/ocitaskserv";
        httpRequest = RestAssured.given();
    }

    private JSONArray getTasks() throws JSONException {
        Response httpResp = httpRequest.header("Accept", "application/json")
                .request(Method.GET, "/tasks");

        Assertions.assertEquals(200, httpResp.getStatusCode());

        JSONObject apiResp = new JSONObject(httpResp.getBody().print());
        JSONArray tasks = apiResp.getJSONArray("tasks");

        Assertions.assertTrue(tasks.length() > 0);
        Assertions.assertTrue(tasks.getJSONObject(0).getLong("id") > 0L);
        Assertions.assertTrue(tasks.getJSONObject(0).getString("title").length() > 0);

        return tasks;
    }

    private long createTask() throws JSONException {
        JSONObject task = new JSONObject();
        task.put("title", "Task from functional test");

        Response httpRespCreate = httpRequest.header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(task.toString())
                .request(Method.POST, "/tasks");

        Assertions.assertEquals(201, httpRespCreate.getStatusCode());

        JSONObject apiRespCreate = new JSONObject(httpRespCreate.getBody().print());

        long taskId = apiRespCreate.getLong("taskId");

        Assertions.assertTrue(taskId > 0L);

        return taskId;
    }

    private long updateTask(long taskId, JSONObject task) throws JSONException {
        Response httpRespCreate = httpRequest.header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .pathParam("id", taskId)
                .body(task.toString())
                .request(Method.PUT, "/tasks/{id}");

        Assertions.assertEquals(200, httpRespCreate.getStatusCode());

        JSONObject apiRespCreate = new JSONObject(httpRespCreate.getBody().print());

        long resultTaskId = apiRespCreate.getLong("taskId");

        Assertions.assertTrue(resultTaskId > 0L);

        return resultTaskId;
    }

    private JSONObject getTask(long taskId) throws JSONException {
        Response httpRespGet = httpRequest.header("Accept", "application/json")
                .pathParam("id", taskId)
                .request(Method.GET, "/tasks/{id}");

        Assertions.assertEquals(200, httpRespGet.getStatusCode());

        JSONObject apiRespGet = new JSONObject(httpRespGet.getBody().print());
        JSONObject taskReceived = apiRespGet.getJSONObject("task");

        Assertions.assertEquals(taskId, taskReceived.getLong("id"));
        Assertions.assertEquals("Task from functional test", taskReceived.getString("title"));

        return taskReceived;
    }

    private void deleteTasks(JSONArray tasks) throws JSONException {
        for(int i = 0; i < tasks.length(); i++) {
            if(StringUtils.equals(tasks.getJSONObject(i).getString("title"), "Task from functional test")) {
                httpRequest = RestAssured.given();
                Response httpRespGet = httpRequest.header("Accept", "application/json")
                        .pathParam("id", tasks.getJSONObject(i).getLong("id"))
                        .request(Method.DELETE, "/tasks/{id}");

                Assertions.assertEquals(200, httpRespGet.getStatusCode());
            }
        }
    }

    @Test
    public void testCreateAndGetTaskSuccess() throws JSONException {
        long taskId = createTask();

        Assertions.assertTrue(taskId > 0L);

        JSONObject taskReceived = getTask(taskId);

        Assertions.assertNotNull(taskReceived);
    }

    @Test
    public void testGetTaskFailedTaskNotFound() throws JSONException {
        Response httpRespGet = httpRequest.header("Accept", "application/json")
                .pathParam("id", 0L)
                .request(Method.GET, "/tasks/{id}");

        Assertions.assertEquals(404, httpRespGet.getStatusCode());
    }

    @Test
    public void testUpdateTasksSuccess() throws JSONException {
        JSONArray tasks = getTasks();

        Assertions.assertNotNull(tasks);

        JSONObject task = tasks.getJSONObject(0);

        long taskId = task.getLong("id");
        task.put("description", "Task from functional test - Description");

        long resultTaskId = updateTask(taskId, task);

        Assertions.assertTrue(resultTaskId > 0L);
    }

    @Test
    public void testDeleteTasksSuccess() throws JSONException {
        JSONArray tasks = getTasks();

        Assertions.assertNotNull(tasks);

        deleteTasks(tasks);
    }

    @Test
    public void testDeleteTasksFailedNoTaskFound() throws JSONException {
        Response httpRespGet = httpRequest.header("Accept", "application/json")
                .pathParam("id", 0L)
                .request(Method.DELETE, "/tasks/{id}");

        Assertions.assertEquals(400, httpRespGet.getStatusCode());
    }

}
