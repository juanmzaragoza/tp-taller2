package tallerii.stories;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import tallerii.stories.controller.LoginController;
import tallerii.stories.network.ConstantsApplicationApiRest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class LoginControllerTest {

    private MockWebServer server;

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

        controller.checkFBUserExists(1, "");

        server.enqueue(new MockResponse().setBody("{\"username\":\"user\"}"));

        verify(mockActivity, timeout(2000).times(1)).showMessage(anyString());
    }

    @Test
    public void testCheckFBUserCallsRegistrationActivity() {
        LoginActivity mockActivity = mock(LoginActivity.class);
        LoginController controller = new LoginController(mockActivity);

        controller.checkFBUserExists(1, "");

        server.enqueue(new MockResponse());

        verify(mockActivity, timeout(2000).times(1)).startRegistrationActivity(anyString());
    }
}
