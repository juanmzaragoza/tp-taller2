package tallerii.stories.controller;

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
import tallerii.stories.LoginActivity;
import tallerii.stories.network.ConstantsApplicationApiRest;
import tallerii.stories.network.apimodels.EntityMetadata;
import tallerii.stories.network.apimodels.LoginResult;
import tallerii.stories.network.apimodels.ServerError;
import tallerii.stories.network.apimodels.Token;

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
    public void testLoginSuccess() {
        LoginActivity mockActivity = mock(LoginActivity.class);
        LoginController controller = new LoginController(mockActivity);
        //TODO pick up from json file, so that a sample response can be easily used to check
        EntityMetadata metadata = new EntityMetadata();
        metadata.setVersion("1");
        Token token = new Token();
        String tokenString = "t0k3n";
        token.setToken(tokenString);
        token.setExpiresAt(Instant.now().plusSeconds(10).toEpochMilli());
        token.setId("10");
        LoginResult loginResult = new LoginResult();
        loginResult.setMetadata(metadata);
        loginResult.setToken(token);
        server.enqueue(new MockResponse().setResponseCode(201).setBody(gson.toJson(loginResult)));
        String username = "username";

        controller.login(username, "password");

        verify(mockActivity, timeout(2000).times(1)).startMainActivity(username, tokenString, "10");
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

        verify(mockActivity, timeout(2000).times(1)).showMessage(errorMessage);
    }
}
