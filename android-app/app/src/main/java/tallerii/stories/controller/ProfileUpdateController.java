package tallerii.stories.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.activities.UserProfileUpdateActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.ApplicationProfile;

public class ProfileUpdateController extends ProfileController {

    public ProfileUpdateController(UserProfileUpdateActivity activity) {
        super(activity);
    }

    /**call api rest and get the user**/
    public void putApplicationProfile(final ApplicationProfile profile) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonElement jsonProfile = new JsonParser().parse(new Gson().toJson(profile));
        Call<JsonObject> responseCall = endpointsApi.putProfileById(profile.getUserId(), jsonProfile.getAsJsonObject());

        responseCall.enqueue(new DefaultCallback<JsonObject>(activity) {
            @Override
            public void onResponse(Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    activity.showMessage("Successfully updated profile");
                    activity.startProfileActivity(profile.getUserId());
                } else {
                    manageErrors(response);
                }
            }
        });
    }
}
