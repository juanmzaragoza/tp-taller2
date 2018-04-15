package tallerii.stories;

import android.widget.Toast;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.Instant;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import tallerii.stories.controller.LoginController;
import tallerii.stories.network.ConstantsApplicationApiRest;
import tallerii.stories.network.apimodels.EntityMetadata;
import tallerii.stories.network.apimodels.LoginResult;
import tallerii.stories.network.apimodels.ServerError;
import tallerii.stories.network.apimodels.Token;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    private MockWebServer server;
    Gson gson = new Gson();

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        ConstantsApplicationApiRest.ROOT_URL = server.url("/").toString();
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testCheckFBUserExistsWithExistingUserShowsError() {
        LoginActivity mockActivity = mock(LoginActivity.class);
        LoginController controller = new LoginController(mockActivity);
        server.enqueue(new MockResponse().setBody("{\"username\":\"user\"}"));

        controller.checkFBUserExists(1, "");

        verify(mockActivity, timeout(2000).times(1)).showMessage(anyString());
    }

    @Test
    public void testSuccessfulCheckFBUserCallsRegistrationActivity() {
        LoginActivity mockActivity = mock(LoginActivity.class);
        LoginController controller = new LoginController(mockActivity);
        server.enqueue(new MockResponse().setResponseCode(404));

        controller.checkFBUserExists(1, "");

        verify(mockActivity, timeout(2000).times(1)).startRegistrationActivity(anyString());
    }

    @Test
    public void testLoginSuccess() {
        LoginActivity mockActivity = mock(LoginActivity.class);
        LoginController controller = new LoginController(mockActivity);
        //TODO pick up from json file, so that a sample response can be easily used to check
        EntityMetadata metadata = new EntityMetadata();
        metadata.setVersion("1");
        Token token = new Token();
        token.setToken("t0k3n");
        token.setExpiresAt(Instant.now().plusSeconds(10).toEpochMilli());
        LoginResult loginResult = new LoginResult();
        loginResult.setMetadata(metadata);
        loginResult.setToken(token);
        server.enqueue(new MockResponse().setResponseCode(201).setBody(gson.toJson(loginResult)));
        String username = "username";

        controller.login(username, "password");

        verify(mockActivity, timeout(2000).times(1)).startMainActivity(username);
    }

    @Test
    public void testLoginErrors() {
        testErrorCode(400);
        testErrorCode(401);
        testErrorCode(500);
    }

    private void testErrorCode(int errorCode) {
        LoginActivity mockActivity = mock(LoginActivity.class);
        LoginController controller = new LoginController(mockActivity);
        String errorMessage = "some error message";
        ServerError error = new ServerError();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        server.enqueue(new MockResponse().setResponseCode(errorCode).setBody(gson.toJson(error)));

        controller.login("username", "password");

        verify(mockActivity, timeout(2000).times(1)).showMessage(errorMessage, Toast.LENGTH_LONG);
    }
}
