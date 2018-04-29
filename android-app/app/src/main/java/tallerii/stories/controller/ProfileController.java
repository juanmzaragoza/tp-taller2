package tallerii.stories.controller;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.UserProfileActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.ApplicationProfile;

public class ProfileController {
    private UserProfileActivity activity;

    public ProfileController(UserProfileActivity activity) {
        this.activity = activity;
    }

    /**call api rest and get the user**/
    public void getUser(final String username) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Call<ApplicationProfile> responseCall = endpointsApi.getProfileById(username);

        responseCall.enqueue(new DefaultCallback<ApplicationProfile>(activity) {
            @Override
            public void onResponse(Response<ApplicationProfile> response) {
                if (response.isSuccessful()) {
                    ApplicationProfile applicationProfile = response.body();
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
