package tallerii.stories.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.activities.SearchUsersActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.Users;

public class ListUsersController {
    private SearchUsersActivity activity;

    public ListUsersController(SearchUsersActivity activity) {
        this.activity = activity;
    }

    /**call api rest and get the requests**/
    public void getUsers(final String userId) {
            EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
            Call<JsonObject> responseCall = endpointsApi.getUsers(userId);

            responseCall.enqueue(new DefaultCallback<JsonObject>(activity) {
                @Override
                public void onResponse(Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Type listType = new TypeToken<ArrayList<Users>>() {}.getType();
                        JsonArray jsonRequests = response.body().getAsJsonArray("users");
                        assert (jsonRequests != null);
                        List<Users> friendRequests = gson.fromJson(jsonRequests, listType);
                        if (friendRequests != null) {
                            activity.populateUsersRecyclerView(friendRequests);
                        }
                    } else {
                        manageErrors(response);
                    }
                }
            });
    }
}
