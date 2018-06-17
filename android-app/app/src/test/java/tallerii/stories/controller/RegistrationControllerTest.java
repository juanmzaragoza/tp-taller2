package tallerii.stories.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import tallerii.stories.activities.RegistrationActivity;
import tallerii.stories.network.ConstantsApplicationApiRest;
import tallerii.stories.network.apimodels.ServerError;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class RegistrationControllerTest {

    private MockWebServer server;
    private Gson gson = new Gson();

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        ConstantsApplicationApiRest.ROOT_URL = server.url("/").toString();
    }

    @After
    public void tearDown() {
        try {
            server.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegistrationCompliesWithSchema() throws InterruptedException {
        RegistrationActivity mockActivity = mock(RegistrationActivity.class);
        RegistrationController controller = new RegistrationController(mockActivity);
        long id = 41235152;
        String username = "pepe";
        String password = "p3p3";

        controller.register(id,username, password);
        RecordedRequest received = server.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("/api/v1/user", received.getPath());
        JsonObject body = bodyFromRequest(received);
        assertEquals(id, body.get("id").getAsLong());
        assertEquals(username, body.get("username").getAsString());
        assertEquals(password, body.get("password").getAsString());
    }

    private JsonObject bodyFromRequest(RecordedRequest request) {
        String json = request.getBody().readUtf8();
        return new JsonParser().parse(json).getAsJsonObject();
    }

    @Test
    public void testRegistrationErrors() {
        testErrorCode(400);
        testErrorCode(401);
        testErrorCode(500);
    }

    private void testErrorCode(int errorCode) {
        RegistrationActivity mockActivity = mock(RegistrationActivity.class);
        RegistrationController controller = new RegistrationController(mockActivity);
        String errorMessage = "some error message";
        ServerError error = new ServerError();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        server.enqueue(new MockResponse().setResponseCode(errorCode).setBody(gson.toJson(error)));

        controller.register(123123,"username", "password");

        verify(mockActivity, timeout(2000).times(1)).showMessage(errorMessage);
    }
}
