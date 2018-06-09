package tallerii.stories.controller;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.MainActivity;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.fragments.main.HomeFragment;
import tallerii.stories.fragments.main.PostStorieFragment;
import tallerii.stories.helpers.ImageHelper;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.Storie;

public class StoriesController {

    protected Fragment fragment;
    private JsonObject parameters;

    public StoriesController(Fragment fragment) {
        this.fragment = fragment;
    }

    Runnable onUploadMedia = new Runnable() {
        @Override
        public void run() {

            EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();

            Call<Storie> responseCall = endpointsApi.postStorie(parameters);

            responseCall.enqueue(new Callback<Storie>() {

                @Override
                public void onFailure(Call<Storie> call, Throwable t) {
                    Log.e("ErrorPostStorie", t.toString());
                }

                @Override
                public void onResponse(Call<Storie> call, Response<Storie> response) {
                    PostStorieFragment fragment = (PostStorieFragment) StoriesController.this.fragment;
                    if (response.isSuccessful()) {
                        Storie storie = response.body();
                        if (storie != null) {
                            StoriesAppActivity activity = (StoriesAppActivity) fragment.getActivity();
                            activity.startMainActivity("The storie was successfully created !!!");
                        }
                    } else{
                        StoriesAppActivity activity = (StoriesAppActivity) StoriesController.this.fragment.getActivity();
                        activity.showMessage("Couldn't create storie. Please, intent you in a few minutes again.", 5);
                    }
                }
            });
        }
    };

    Runnable onUploadMediaError = new Runnable() {
        @Override
        public void run() {
            Log.e("ErrorUploadFirebase","Error on upload media to firebase");
        }
    };

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
    public void publishStorie(Uri uriMedia, String title, String description, boolean visibility, String location, String storyType) {

        ImageHelper imageHelper = new ImageHelper(this.fragment.getContext());
        MainActivity mainActivity = (MainActivity)fragment.getActivity();
        Date currentTime = Calendar.getInstance().getTime();

        parameters = new JsonObject();
        parameters.addProperty("_id", "");
        parameters.addProperty("_rev", "");
        parameters.addProperty("created_time", currentTime.toString());
        parameters.addProperty("description", description);
        parameters.addProperty("location", location);
        parameters.addProperty("storyType", storyType);
        parameters.addProperty("title", title);
        parameters.addProperty("multimedia", UUID.randomUUID().toString()); //TODO: upload to firebase before and get id
        parameters.addProperty("updated_time", currentTime.toString());
        parameters.addProperty("userId", mainActivity.getProfile().getId());
        parameters.addProperty("visibility", visibility? "public":"private");

        imageHelper.uploadMedia(UUID.randomUUID().toString(), uriMedia, onUploadMedia, onUploadMediaError);

    }


}
