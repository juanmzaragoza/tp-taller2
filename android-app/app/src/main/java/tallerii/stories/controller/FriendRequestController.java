package tallerii.stories.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.activities.FriendRequestActivity;
import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.helpers.FriendRequestAdapter;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.FriendRequest;

public class FriendRequestController {
    private FriendRequestActivity activity;
    private Gson gson = new Gson();

    public FriendRequestController(FriendRequestActivity activity) {
        this.activity = activity;
    }

    /**call api rest and get the requests**/
    public void getFriendRequests(final String userId) {
            EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
            Call<JsonObject> responseCall = endpointsApi.getFriendRequestsByUserId(userId);

            responseCall.enqueue(new DefaultCallback<JsonObject>(activity) {
                @Override
                public void onResponse(Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Type listType = new TypeToken<ArrayList<FriendRequest>>() {}.getType();
                        JsonArray jsonRequests = response.body().getAsJsonArray("requests");
                        assert (jsonRequests != null);
                        List<FriendRequest> friendRequests = gson.fromJson(jsonRequests, listType);
                        if (friendRequests != null) {
                            activity.populateFriendsRecyclerView(friendRequests);
                        }
                    } else {
                        manageErrors(response);
                    }
                }
            });
    }

    public void acceptFriendRequest(final FriendRequest friendRequest, final FriendRequestAdapter adapter) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject request = new JsonObject();
        request.addProperty("request_id", friendRequest.getId());
        Call<JsonObject> responseCall = endpointsApi.acceptFriendRequest(request);
        setOnSuccessRemove(responseCall, adapter, friendRequest.getId(), "Request Accepted");
        String userName = StoriesLoggedInActivity.getProfile().getName();
        String message = String.format(Locale.getDefault(), "%s has accepted your friendship request!", userName);
        FCMNotificationController.sendNotification(activity, friendRequest.getSenderUserId(), message);
    }

    private void setOnSuccessRemove(Call<JsonObject> responseCall, final FriendRequestAdapter adapter, final String requestId, final String onSuccessMessage) {
        responseCall.enqueue(new DefaultCallback<JsonObject>(activity) {
            @Override
            public void onResponse(Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.removeRequest(requestId);
                    activity.showMessage(onSuccessMessage);
                    activity.updateProfile();
                } else {
                    manageErrors(response);
                }
            }
        });
    }

    public void declineFriendRequest(final String requestId, final FriendRequestAdapter adapter) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Call<JsonObject> responseCall = endpointsApi.declineFriendRequest(requestId);
        setOnSuccessRemove(responseCall, adapter, requestId, "Request Declined");
    }
}
