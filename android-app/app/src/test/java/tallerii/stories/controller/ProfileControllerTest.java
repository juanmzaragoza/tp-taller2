package tallerii.stories.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import tallerii.stories.UserProfileActivity;
import tallerii.stories.network.ConstantsApplicationApiRest;
import tallerii.stories.network.apimodels.ApplicationProfile;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProfileControllerTest {

    private MockWebServer server;
    private String mockProfile;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        ConstantsApplicationApiRest.ROOT_URL = server.url("/").toString();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("mockResponses").getFile());
            JSONObject apiMockResponses = new JSONObject(new String(Files.readAllBytes(file.toPath())));
            mockProfile = apiMockResponses.getJSONObject("ApplicationProfile").toString();
            mockProfile = String.format("{" +
                    "\"metadata\": {\n" +
                    "    \"version\": \"1.0\"\n" +
                    "  }, \n" +
                    "  \"profile\": %s}", mockProfile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testGetUserSuccess() throws InterruptedException {
        UserProfileActivity mockActivity = mock(UserProfileActivity.class);
        ProfileController controller = new ProfileController(mockActivity);
        server.enqueue(new MockResponse().setBody(mockProfile));

        controller.getUser("nico");

        verify(mockActivity, timeout(2000).times(1)).initializeProfile(any(ApplicationProfile.class));
        final RecordedRequest request = server.takeRequest();
        assertEquals("/api/v1/profiles/nico", request.getPath());
    }
}
