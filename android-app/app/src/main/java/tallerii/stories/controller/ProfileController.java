package tallerii.stories.controller;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.ApplicationProfile;

public class ProfileController {
    protected StoriesLoggedInActivity activity;

    public ProfileController(StoriesLoggedInActivity activity) {
        this.activity = activity;
    }

    /**call api rest and get the user**/
    public void getUser(final String username) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Call<JsonObject> responseCall = endpointsApi.getProfileById(username);

        responseCall.enqueue(new DefaultCallback<JsonObject>(activity) {
            @Override
            public void onResponse(Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApplicationProfile applicationProfile = gson.fromJson(response.body().getAsJsonObject("profile"), ApplicationProfile.class);
                    if (applicationProfile != null) {
                        activity.initializeProfile(applicationProfile);
                    }
                } else {
                    manageErrors(response);
                }
            }
        });
    }
}
