package tallerii.stories.controller;

import android.app.DialogFragment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.StoriesLoggedInActivity;
import tallerii.stories.fragments.main.StorieCommentsDialogFragment;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.Comment;
import tallerii.stories.network.apimodels.Storie;

public class CommentController {

    private final StorieCommentsDialogFragment fragment;

    public CommentController(StorieCommentsDialogFragment fragment){
        this.fragment = fragment;
    }

    public void doComment(String storieId, String comment) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();

        parameters.addProperty("storieId", storieId);
        parameters.addProperty("userId", StoriesLoggedInActivity.getProfile().getId());
        parameters.addProperty("message", comment);
        parameters.addProperty("date", "");
        parameters.addProperty("_id", "");
        parameters.addProperty("_rev", "");

        Call<Comment> responseCall = endpointsApi.postStorieComment(parameters);
        responseCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                // if username and password match
                if (response.isSuccessful() && response.code() == 200) {
                    /* Response
                     *  {
                     *      "storie_id": "5ae66a31d4ef925dac59a97b",
                     *      "user_id": "1",
                     *      "_rev": "",
                     *      "date": "",
                     *      "_id": "21cd6a03e9874b5ba0d5b27ae9c79793",
                     *      "message": "Comment 1"
                     *  }
                     */
                    Comment comment = response.body();
                    String message = comment.getMessage();
                    String userId = comment.getUserId();
                    String storieId = comment.getStorieId();
                    fragment.onSuccessComment(userId, message, storieId);
                } else {
                    fragment.onErrorComment(response.message());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                fragment.onErrorComment("An error has ocurred while fetching comments");
            }
        });
    }
}
