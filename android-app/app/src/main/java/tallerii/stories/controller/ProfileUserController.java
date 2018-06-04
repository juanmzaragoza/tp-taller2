package tallerii.stories.controller;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.UserProfileActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.FriendRequest;

public class ProfileUserController extends ProfileController{
    private UserProfileActivity userProfileActivity;

    public ProfileUserController(UserProfileActivity activity) {
        super(activity);
        this.userProfileActivity = activity;
    }

    public void requestFriendship(final FriendRequest request) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Call<JsonObject> responseCall = endpointsApi.postFriendRequest(request);

        responseCall.enqueue(new DefaultCallback<JsonObject>(userProfileActivity) {
            @Override
            public void onResponse(Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    userProfileActivity.deactivateFriendButton();
                } else {
                    manageErrors(response);
                }
            }
        });
    }
}
