package tallerii.stories.controller;

import com.google.gson.Gson;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import tallerii.stories.activities.UserProfileUpdateActivity;
import tallerii.stories.network.ConstantsApplicationApiRest;
import tallerii.stories.network.apimodels.ApplicationProfile;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class ProfileUpdateControllerTest {
    private MockWebServer server;
    private Gson gson = new Gson();

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
    public void testPutUserSuccess() throws InterruptedException, JSONException {
        UserProfileUpdateActivity mockActivity = mock(UserProfileUpdateActivity.class);
        ProfileUpdateController controller = new ProfileUpdateController(mockActivity);
        ApplicationProfile profile = new ApplicationProfile();
        profile.setUserId("1234");
        profile.setFirstName("nico");
        profile.setLastName("dom");
        profile.setProfilePicture("image/pic");
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));

        controller.putApplicationProfile(profile);

        verify(mockActivity, timeout(2000).times(1)).showMessage("Successfully updated profile");
        final RecordedRequest request = server.takeRequest();
        assertEquals("/api/v1/profiles/1234", request.getPath());
        JSONAssert.assertEquals(gson.toJson(profile), request.getBody().readUtf8(), JSONCompareMode.LENIENT);
    }
}
