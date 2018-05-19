package tallerii.stories.controller;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.fragments.main.HomeFragment;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.Story;

public class StoriesController {
    protected HomeFragment fragment;

    public StoriesController(HomeFragment fragment) {
        this.fragment = fragment;
    }

    /*
     * Call api rest and get the stories
     *
     * @param userId Logged in user id
     *
     */
    public void getStories(final String userId) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Call<List<Story>> responseCall = endpointsApi.getStoriesByUserId(userId);

        responseCall.enqueue(new Callback<List<Story>>() {

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Log.e("Error", t.toString());
            }

            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if (response.isSuccessful()) {
                    List<Story> stories = response.body();
                    if (stories != null) {
                        fragment.populateStories(stories);
                    }
                }
            }
        });
    }
}
