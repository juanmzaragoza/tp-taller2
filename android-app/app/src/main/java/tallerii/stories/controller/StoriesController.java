package tallerii.stories.controller;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.MainActivity;
import tallerii.stories.fragments.main.HomeFragment;
import tallerii.stories.fragments.main.PostStorieFragment;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.Storie;

public class StoriesController {
    protected Fragment fragment;

    public StoriesController(Fragment fragment) {
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
                HomeFragment fragment = (HomeFragment) StoriesController.this.fragment;
                if (response.isSuccessful()) {
                    List<Storie> stories = response.body();
                    if (stories != null) {
                        fragment.populateStories(stories);
                    }
                }
            }
        });
    }

    /*
     * Call api rest and get the stories
     *
     * @param userId Logged in user id
     *
     */
    public void publishStorie(Bitmap pictureBitmap, String title, String description, boolean visibility, String location, String storyType) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Date currentTime = Calendar.getInstance().getTime();
        MainActivity mainActivity = (MainActivity)fragment.getActivity();

        JsonObject parameters = new JsonObject();
        parameters.addProperty("created_time", currentTime.toString());
        parameters.addProperty("description", description);
        parameters.addProperty("location", location);
        parameters.addProperty("multimedia", /*pictureBitmap*/"1233-3231-3123-1231"); //TODO: upload to firebase before and get id
        parameters.addProperty("storyType", storyType);
        parameters.addProperty("title", title);
        parameters.addProperty("updated_time", currentTime.toString());
        parameters.addProperty("userId", mainActivity.getProfile().getId());
        parameters.addProperty("userId", visibility? "public":"private");

        Call<Storie> responseCall = endpointsApi.postStorie(parameters);

        responseCall.enqueue(new Callback<Storie>() {

            @Override
            public void onFailure(Call<Storie> call, Throwable t) {
                Log.e("Error", t.toString());
            }

            @Override
            public void onResponse(Call<Storie> call, Response<Storie> response) {
                PostStorieFragment fragment = (PostStorieFragment) StoriesController.this.fragment;
                if (response.isSuccessful()) {
                    Storie storie = response.body();
                    if (storie != null) {
                        //fragment.populateStories(stories);
                    }
                }
            }
        });
    }


}
