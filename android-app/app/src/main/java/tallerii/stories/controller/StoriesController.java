package tallerii.stories.controller;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.activities.StoriesAppActivity;
import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.helpers.ImageHelper;
import tallerii.stories.interfaces.StoriesAware;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.Storie;

public class StoriesController {
    protected StoriesAware context;
    private JsonObject parameters;

    public StoriesController(StoriesAware context) {
        this.context = context;
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
                    if (response.isSuccessful()) {
                        Storie storie = response.body();
                        if (storie != null) {
                            StoriesAppActivity activity = context.getLoggedInActivity();
                            activity.startMainActivity("The storie was successfully created !!!");
                        }
                    } else{
                        StoriesAppActivity activity = context.getLoggedInActivity();
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
                StoriesAppActivity activity = context.getLoggedInActivity();
                activity.showMessage("Cannot load stories! Please, retry in a minutes", 5);
            }

            @Override
            public void onResponse(Call<List<Storie>> call, Response<List<Storie>> response) {
                if (response.isSuccessful()) {
                    List<Storie> stories = response.body();
                    if (stories != null) {
                        context.populateStories(stories);
                    }
                }
            }
        });
    }


    public void getFlashStories(final String userId) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Call<List<Storie>> responseCall = endpointsApi.getStoriesByUserId(userId,"fast");

        responseCall.enqueue(new Callback<List<Storie>>() {

            @Override
            public void onFailure(Call<List<Storie>> call, Throwable t) {
                Log.e("Error", t.toString());
                StoriesAppActivity activity = context.getLoggedInActivity();
                activity.showMessage("Cannot load flash stories! Please, retry in a minutes", 5);
            }

            @Override
            public void onResponse(Call<List<Storie>> call, Response<List<Storie>> response) {
                if (response.isSuccessful()) {
                    List<Storie> stories = response.body();
                    if (stories != null) {
                        context.populateStories(stories);
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

        ImageHelper imageHelper = new ImageHelper(context.getLoggedInActivity());
        Date currentTime = Calendar.getInstance().getTime();
        String multimediaName = UUID.randomUUID().toString();

        parameters = new JsonObject();
        parameters.addProperty("_id", "");
        parameters.addProperty("_rev", "");
        //parameters.addProperty("created_time", currentTime.toString());
        parameters.addProperty("description", description);
        parameters.addProperty("location", location);
        parameters.addProperty("story_type", storyType);
        parameters.addProperty("title", title);
        parameters.addProperty("multimedia", multimediaName);
        parameters.addProperty("updated_time", currentTime.toString());
        parameters.addProperty("user_id", context.getLoggedInActivity().getProfile().getId());
        parameters.addProperty("visibility", visibility? "public":"private");

        imageHelper.uploadMedia(multimediaName, uriMedia, onUploadMedia, onUploadMediaError);

    }


    public void changeReaction(String storieId, String reactionName) {

        Date currentTime = Calendar.getInstance().getTime();

        parameters = new JsonObject();
        parameters.addProperty("_id", "");
        parameters.addProperty("_rev", "");
        parameters.addProperty("date",currentTime.toString());
        parameters.addProperty("storie_id", storieId);
        parameters.addProperty("user_id", StoriesLoggedInActivity.getProfile().getId());
        parameters.addProperty("reaction", reactionName);

        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        Call<Storie> responseCall = endpointsApi.postStorieReaction(parameters);

        responseCall.enqueue(new Callback<Storie>() {

            @Override
            public void onFailure(Call<Storie> call, Throwable t) {
                Log.e("PostStorieReaction", t.toString());
            }

            @Override
            public void onResponse(Call<Storie> call, Response<Storie> response) {
                if (response.isSuccessful()) {
                    Storie storie = response.body();
                    if (storie != null) {
                        //StoriesAppActivity activity = context.getLoggedInActivity();
                        //activity.startMainActivity("You has reacted !!!");
                    }
                } else{
                    StoriesAppActivity activity = context.getLoggedInActivity();
                    activity.showMessage("Couldn't react to storie. Please, try again in a few minutes.", 5);

                }
            }
        });

    }
}
