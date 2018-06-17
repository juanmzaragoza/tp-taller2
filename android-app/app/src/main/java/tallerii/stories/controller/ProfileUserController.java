package tallerii.stories.controller;

import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.activities.UserProfileActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.FriendRequest;
import tallerii.stories.network.apimodels.ServerError;

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
                    userProfileActivity.showMessage("Request sent!");
                    FCMNotificationController.sendNotification(userProfileActivity, request.getRcvUserId(),
                            String.format("%s has sent a friend request!", request.getFullName()));
                } else {
                    try {
                        ServerError error = gson.fromJson(response.errorBody().string(), ServerError.class);
                        if (error.getMessage().contains("already exists")) {
                            activity.showMessage("Already requested friendship.");
                        } else {
                            activity.showMessage(error.getMessage());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void unfriend(final String friendshipId, final String friendUserId) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Call<JsonObject> responseCall = endpointsApi.unfriend(friendshipId);

        responseCall.enqueue(new DefaultCallback<JsonObject>(userProfileActivity) {
            @Override
            public void onResponse(Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    userProfileActivity.showMessage("Unfriended", 10);
                    userProfileActivity.startProfileActivity(friendUserId);
                    userProfileActivity.updateProfile();
                } else {
                    manageErrors(response);
                }
            }
        });
    }
}
