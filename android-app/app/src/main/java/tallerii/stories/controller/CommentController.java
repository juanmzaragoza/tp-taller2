package tallerii.stories.controller;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.fragments.main.StorieCommentsDialogFragment;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.Comment;

public class CommentController {

    private final StorieCommentsDialogFragment fragment;

    public CommentController(StorieCommentsDialogFragment fragment){
        this.fragment = fragment;
    }

    public void doComment(String storieId, String comment) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();

        parameters.addProperty("storie_id", storieId);
        parameters.addProperty("user_id", StoriesLoggedInActivity.getProfile().getId());
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

    public void getCommentsByStorie(String storieId) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();

        parameters.addProperty("storie_id", storieId);

        Call<List<Comment>> responseCall = endpointsApi.getStorieComments(storieId);
        responseCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
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
                    List<Comment> comments = response.body();
                    if (comments != null) {
                        fragment.populateComments(comments);
                    }
                } else {
                    fragment.onErrorComment(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                fragment.onErrorComment("An error has ocurred while fetching comments");
            }
        });
    }
}
