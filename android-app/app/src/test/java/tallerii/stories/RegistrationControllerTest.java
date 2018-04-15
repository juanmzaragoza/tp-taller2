package tallerii.stories;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import tallerii.stories.controller.RegistrationController;
import tallerii.stories.network.ConstantsApplicationApiRest;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class RegistrationControllerTest {

    private MockWebServer server;

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
        String username = "pepe";
        String password = "p3p3";

        controller.register(username, password);
        RecordedRequest received = server.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("/user", received.getPath());
        JsonObject body = bodyFromRequest(received);
        assertEquals(username, body.get("username").getAsString());
        assertEquals(password, body.get("password").getAsString());
    }

    private JsonObject bodyFromRequest(RecordedRequest request) {
        String json = request.getBody().readUtf8();
        return new JsonParser().parse(json).getAsJsonObject();
    }

}
