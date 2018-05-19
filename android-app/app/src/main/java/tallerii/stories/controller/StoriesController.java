package tallerii.stories.controller;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.fragments.main.HomeFragment;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.Storie;

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
        Call<List<Storie>> responseCall = endpointsApi.getStoriesByUserId(userId);

        responseCall.enqueue(new Callback<List<Storie>>() {

            @Override
            public void onFailure(Call<List<Storie>> call, Throwable t) {
                Log.e("Error", t.toString());
            }

            @Override
            public void onResponse(Call<List<Storie>> call, Response<List<Storie>> response) {
                if (response.isSuccessful()) {
                    List<Storie> stories = response.body();
                    if (stories != null) {
                        fragment.populateStories(stories);
                    }
                }
            }
        });
    }
}
